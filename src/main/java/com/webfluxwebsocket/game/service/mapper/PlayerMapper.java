package com.webfluxwebsocket.game.service.mapper;

import com.webfluxwebsocket.game.model.Player;

public interface PlayerMapper {

    String toJson(Player player);

    Player fromJson(String json);
}
