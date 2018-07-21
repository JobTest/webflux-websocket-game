package com.webfluxwebsocket.game.service.mapper;

import com.webfluxwebsocket.game.domain.Player;

public interface PlayerMapper {

    String toJson(Player player);

    Player fromJson(String json);
}
