package com.game.service;

import com.game.entity.Player;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PlayerService {

    // 1. Get players list
    // Получение списка игроков с параметрами
    List<Player> getPlayersList(Map<String, String> params);

    // Получение полного списка
    List<Player> getAll(Pageable pageable);

    // 2. Get players count
    // Получение количества игроков с параметрами
    Integer getPlayersCount(Map<String, String> params);

    // Получение количества игроков без параметрами
    Integer getCount();

    // 3. Create player
    // создание нового игрока
    Player createPlayer(Map<String, String> params);

    // 4. Get player
    // Получение игрока по id
    Player getPlayer(Long id);

    // 5. Update player
    // Обновление игрока по id
    Player updatePlayer(Long id, Map<String, String> params);

    // 6. Delete player
    // Удаление игрока по id
    String deletePlayer(Long id);


    // Проверка параметров на валидность
    boolean existsById(Long id);
    boolean isIdValid(Long id);
    boolean isParamsValid(Map<String, String> params);
    boolean isAllParamsFound(Map<String, String> params);

}
