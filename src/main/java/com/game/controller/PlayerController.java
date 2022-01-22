
package com.game.controller;

import com.game.entity.Player;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.MappedSuperclass;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    // REST API
    // чтение списка игроков @Get players list /rest/players/
    @GetMapping()
    public @ResponseBody ResponseEntity<List<Player>> getPlayersList(@RequestParam Map<String, String> params){
        return params.isEmpty()
                ? new ResponseEntity<>(playerService.getAll(PageRequest.of(0, 3)), HttpStatus.OK)
                : new ResponseEntity<>(playerService.getPlayersList(params), HttpStatus.OK);
    }

    // чтение количества игроков @Get players count /rest/players/count
    @GetMapping("/count")
    public @ResponseBody Integer getPlayersCount(@RequestParam Map<String, String> params){
        return params.isEmpty()
                ? playerService.getCount()
                : playerService.getPlayersCount(params);
    }

    // Создание нового игрока @Create player /rest/players
    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody ResponseEntity<Player> createPlayer(@RequestBody Map<String, String> params){
        if (!playerService.isAllParamsFound(params) || !playerService.isParamsValid(params))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Player player = playerService.createPlayer(params);

        return player == null
                ? new ResponseEntity<>(HttpStatus.BAD_REQUEST)
                : new ResponseEntity<>(player, HttpStatus.OK);
    }

    // Получение игрока по его id @Get player /rest/players/{id}
    @GetMapping(value = "/{id}")
    public @ResponseBody ResponseEntity<Player> getPlayer(@PathVariable(name = "id") Long id){
        try {
            if (!playerService.isIdValid(id)){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (!playerService.existsById(id)){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(playerService.getPlayer(id), HttpStatus.OK);
            }

        } catch (NullPointerException | IllegalArgumentException exception){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Обновление игрока по его id @Update player /rest/players/{id}
    @PostMapping(value = "/{id}")
    public @ResponseBody ResponseEntity<Player> updatePlayer(@PathVariable(name = "id") Long id,
                                    @RequestBody Map<String, String> params){
        if (!playerService.isIdValid(id) || !playerService.isParamsValid(params)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Player result = playerService.updatePlayer(id, params);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }

    }

    // Удаление игрока по его id @Delete player /rest/players/{id}
    @DeleteMapping(value = "/{id}")
    public @ResponseBody ResponseEntity<?> deletePlayer(@PathVariable(name = "id") Long id){
        try {
            if (!playerService.isIdValid(id))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            String result = playerService.deletePlayer(id);
            if ("400".equals(result))
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            if ("404".equals(result))
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            if ("200 OK".equals(result))
                return new ResponseEntity<>(HttpStatus.OK);
        } catch (NullPointerException | IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}


