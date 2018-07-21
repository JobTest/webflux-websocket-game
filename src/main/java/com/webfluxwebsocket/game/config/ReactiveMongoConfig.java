package com.webfluxwebsocket.game.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories("com.webfluxwebsocket.game.repository")
public class ReactiveMongoConfig {

}
