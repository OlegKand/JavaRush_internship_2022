package com.game.service;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService{

    @Autowired
    private PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    // Получение списка игроков с подходящими параметрами
    @Override
    public List<Player> getPlayersList(Map<String, String> params) {
        String name = params.getOrDefault("name", null);
        String title = params.getOrDefault("title", null);
        Race race = params.containsKey("race")
                ? Race.valueOf(params.get("race"))
                : null;
        Profession profession = params.containsKey("profession")
                ? Profession.valueOf(params.get("profession"))
                : null;
        Date after = params.containsKey("after")
                ? new Date(Long.parseLong(params.get("after")))
                : null;
        Date before = params.containsKey("before")
                ? new Date(Long.parseLong(params.get("before")))
                : null;
        Boolean banned = params.containsKey("banned")
                ? Boolean.parseBoolean(params.get("banned"))
                : null;
        Integer minExperience = params.containsKey("minExperience")
                ? Integer.parseInt(params.get("minExperience"))
                : null;
        Integer maxExperience = params.containsKey("maxExperience")
                ? Integer.parseInt(params.get("maxExperience"))
                : null;
        Integer minLevel = params.containsKey("minLevel")
                ? Integer.parseInt(params.get("minLevel"))
                : null;
        Integer maxLevel = params.containsKey("maxLevel")
                ? Integer.parseInt(params.get("maxLevel"))
                : null;

        int pageNumber = Integer.parseInt(params.getOrDefault("pageNumber", "0"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "3"));

        Pageable pageable = params.containsKey("order")
                ? PageRequest.of(pageNumber, pageSize, Sort.Direction.ASC, PlayerOrder.valueOf(params.get("order")).getFieldName())
                : PageRequest.of(pageNumber, pageSize);

        return playerRepository.getAllByParams(name, title, race, profession, after,
                before, banned, minExperience, maxExperience, minLevel, maxLevel,
                pageable).stream().collect(Collectors.toList());
    }

    // Получение полного списка игроков
    @Override
    public List<Player> getAll(Pageable pageable) {
        return playerRepository.findAll(pageable).stream().collect(Collectors.toList());
    }

    // Получение количества игроков с подходящими параметрами
    /*  Optional:
            String name,            String title,
            Race race,              Profession profession,
            Long after,             Long before,
            Boolean banned,
            Integer minExperience,  Integer maxExperience,
            Integer minLevel,       Integer maxLevel
     */
    @Override
    public Integer getPlayersCount(Map<String, String> params) {
        String name = params.getOrDefault("name", null);
        String title = params.getOrDefault("title", null);
        Race race = params.containsKey("race")
                ? Race.valueOf(params.get("race"))
                : null;
        Profession profession = params.containsKey("profession")
                ? Profession.valueOf(params.get("profession"))
                : null;
        Date after = null;
                if (params.containsKey("after")) {
                    after = new Date(Long.parseLong(params.get("after")));
                }
        Date before = null;
                if (params.containsKey("before")) {
                    before = new Date(Long.parseLong(params.get("before")));
                }
        Boolean banned = params.containsKey("banned")
                ? Boolean.parseBoolean(params.get("banned"))
                : null;
        Integer minExperience = params.containsKey("minExperience")
                ? Integer.parseInt(params.get("minExperience"))
                : null;
        Integer maxExperience = params.containsKey("maxExperience")
                ? Integer.parseInt(params.get("maxExperience"))
                : null;
        Integer minLevel = params.containsKey("minLevel")
                ? Integer.parseInt(params.get("minLevel"))
                : null;
        Integer maxLevel = params.containsKey("maxLevel")
                ? Integer.parseInt(params.get("maxLevel"))
                : null;
        return playerRepository.countByParams(name, title, race, profession, after,
                before, banned, minExperience, maxExperience, minLevel, maxLevel);
    }

    // Получение количества игроков

    @Override
    public Integer getCount() {
        try {
            return Math.toIntExact(playerRepository.count());
        } catch (ArithmeticException exception){
            return Integer.MAX_VALUE;
        }
    }

    // Создание нового игрока
    @Override
    public Player createPlayer(Map<String, String> params) {
        try {
            String name = params.getOrDefault("name", null);
            String title = params.getOrDefault("title", null);
            Race race = Race.valueOf(params.get("race"));
            Profession profession = Profession.valueOf(params.get("profession"));
            Integer experience = Integer.parseInt(params.get("experience"));
            Date birthday = new Date(Long.parseLong(params.get("birthday")));
            Boolean banned = params.containsKey("banned")
                    && "true".equals(params.get("banned"));

            Player player = new Player(name, title, race, profession,
                    birthday, banned, experience);

            player.updateLevelAndExp();
            return playerRepository.save(player);
        } catch (NullPointerException | IllegalArgumentException | ClassCastException exception) {
            return null;
        }
    }

    // Получение игрока по id
    @Override
    public Player getPlayer(Long id) {
        return playerRepository.findById(id).get();
    }

    // Обновление данных об игроке
    @Override
    public Player updatePlayer(Long id, Map<String, String> params) {
        // проверка условия валидности игрока
        if (!playerRepository.findById(id).isPresent() || params == null) return null;
        Player updatePlayer = playerRepository.findById(id).get();

        String name = params.getOrDefault("name", null);
        String title = params.getOrDefault("title", null);
        Race race = params.containsKey("race")
                ? Race.valueOf(params.get("race"))
                : null;
        Profession profession = params.containsKey("profession")
                ? Profession.valueOf(params.get("profession"))
                : null;
        Date birthday = params.containsKey("birthday")
                ? new Date(Long.parseLong(params.get("birthday")))
                : null;
        Boolean banned = params.containsKey("banned")
                ? Boolean.parseBoolean(params.get("banned"))
                : null;
        Integer experience = params.containsKey("experience")
                ? Integer.parseInt(params.get("experience"))
                : null;

        if (name != null) updatePlayer.setName(name);
        if (title != null) updatePlayer.setTitle(title);
        if (race != null) updatePlayer.setRace(race);
        if (profession != null) updatePlayer.setProfession(profession);
        if (birthday != null) updatePlayer.setBirthday(birthday);
        if (banned != null) updatePlayer.setBanned(banned);
        if (experience != null) updatePlayer.setExperience(experience);

        updatePlayer.updateLevelAndExp();
        playerRepository.saveAndFlush(updatePlayer);
        return updatePlayer;
    }

    // Удаление игрока по id
    @Override
    public String deletePlayer(Long id) {
        if (!playerRepository.existsById(id)) {
            return "404";
        } else if (!isIdValid(id)){
            return "400";
        } else {
            playerRepository.deleteById(id);
            return "200 OK";
        }
    }

    // Проверка на наличие в базе id игрока
    @Override
    public boolean existsById(Long id) {
        return playerRepository.existsById(id);
    }

    // Проверка валидности передаваемого id игрока
    @Override
    public boolean isIdValid(Long id) {
        return Math.ceil(id) == id && id > 0;
    }

    // Провекра валидности всех передаваемых параметров
    /*
    - длина значения параметра “name” или “title” превышает
    размер соответствующего поля в БД (12 и 30 символов);
    - значение параметра “name” пустая строка;
    - опыт находится вне заданных пределов;
    - “birthday”:[Long] < 0;
    - дата регистрации находятся вне заданных пределов.
     */
    @Override
    public boolean isParamsValid(Map<String, String> params) {
        String name = params.getOrDefault("name", null);
        String title = params.getOrDefault("title", null);

        Integer birthDate = null;
        if (params.containsKey("birthday")){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(params.get("birthday")));
            birthDate = calendar.get(Calendar.YEAR);
        }

        Integer experience = params.containsKey("experience")
                ? Integer.parseInt(params.get("experience"))
                : null;

        boolean resultValid =
                (name == null || title == null || name.length() <= 12 && name.length() > 0 &&
                title.length() <= 30 && title.length() > 0)
                && (birthDate == null || birthDate >= 2000 && birthDate <= 3000)
                && (experience == null || experience >= 0 && experience <= 10_000_000);
        try {
            if (params.containsKey("race") && params.containsKey("profession")){
                Profession profession = Profession.valueOf(params.get("profession"));
                Race race = Race.valueOf(params.get("race"));
            }
        } catch (IllegalArgumentException | NullPointerException exception){
            resultValid = false;
        }
        return resultValid;
    }

    // Проверка на наличие всех необходимых для создания объекта параметров
    @Override
    public boolean isAllParamsFound(Map<String, String> params) {
        return params.containsKey("name") &&
               params.containsKey("title") &&
               params.containsKey("race") &&
               params.containsKey("profession") &&
               params.containsKey("birthday") &&
               params.containsKey("experience");
    }
}
