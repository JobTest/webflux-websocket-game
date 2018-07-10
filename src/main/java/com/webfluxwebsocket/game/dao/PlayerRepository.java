package com.webfluxwebsocket.game.dao;

import com.webfluxwebsocket.game.model.Player;
import com.webfluxwebsocket.game.model.enumeration.PlayerState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

public class PlayerRepository {

    private final ConcurrentSkipListMap<String, Player> players = new ConcurrentSkipListMap<>();

    public Player findOne(String id) {
        return players.get(id);
    }

    public List<Player> findAll() {
        return new ArrayList<>(players.values());
    }

    public List<Player> findByStates(PlayerState... states) {
        return players.values().parallelStream()
                .filter(gamePlayer -> Arrays.asList(states).contains(gamePlayer.getState()))
                .collect(Collectors.toList());
    }

    public Player save(Player player) {
        player.setId(player.getId());
        return players.put(player.getId(), player);
    }
}
