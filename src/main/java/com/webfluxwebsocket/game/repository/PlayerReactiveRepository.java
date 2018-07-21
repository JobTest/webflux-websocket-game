package com.webfluxwebsocket.game.repository;

import com.webfluxwebsocket.game.domain.Player;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerReactiveRepository extends ReactiveMongoRepository<Player, String> {

}
