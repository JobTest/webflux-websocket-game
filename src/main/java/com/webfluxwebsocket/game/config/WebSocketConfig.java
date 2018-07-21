package com.webfluxwebsocket.game.config;

import com.webfluxwebsocket.game.domain.Player;
import com.webfluxwebsocket.game.domain.enumeration.PlayerState;
import com.webfluxwebsocket.game.service.GameEngineService;
import com.webfluxwebsocket.game.service.mapper.PlayerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableScheduling
public class WebSocketConfig {

    @Autowired
    private GameEngineService gameEngineService;

    @Autowired
    private PlayerMapper playerMapper;

    @Value("/${game.ws-host}")
    private String webSocketHostName;

    /**
     * Список объектов - это тоже самое что просто обект.
     * В Java, если это объект тогда мы получаем только ссылку на этот объект (то есть, объект является единственной и общей точкое для всех...):
     * - это значит если объект был изменен в теле одной функции, то изменения этого объекта будут видны по ссылке и в теле других функций БЕЗ ПЕРЕЗАГРУЗКИ ДАННЫХ.
     *
     * А если данные хранятся в базе данных (на удаленном сервере) в этом случае :
     * - если объект в базе данных был изменен в теле одной функции, то изменения этого объекта УЖЕ НЕ БУДУТ ВИДНЫ в теле других функций БЕЗ ПЕРЕЗАГРУЗКИ ДАННЫХ.
     * И поэтому здесь нужно перезагрузить данные...
     */
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public HandlerMapping getUrlHandlerMapping() {
        Map<String, WebSocketHandler> socketHandlers = new HashMap<>();

        gameEngineService.findByStates(PlayerState.START_ROUND, PlayerState.PLAY, PlayerState.STOP_ROUND)
                .map(player -> {
                        String urlSocketChanel = webSocketHostName + "/" + player.getId();

                        socketHandlers.put(urlSocketChanel,
                                socketSession -> socketSession.send( Flux.<String>generate(sink -> {
                                            Player monoPlayer = gameEngineService.findById(player.getId())
                                                    .block();

                                            switch (monoPlayer.getState()) {
                                                case PLAY:
                                                    sink.next(playerMapper.toJson(monoPlayer));
                                                    break;
                                                case STOP_ROUND:
                                                    sink.next(playerMapper.toJson(monoPlayer));
                                                    break;
                                                case START_ROUND:
                                                    sink.complete();
                                                    break; } })
                                        .map( socketSession::textMessage )
                                        .delayElements(Duration.ofSeconds(1))));
                        return player; })
                .then()   //todo .then() <<< Void  |  .collectList() <<< List
                .block(); //todo .block() <<< Mono

        SimpleUrlHandlerMapping urlHandlerMapping = new SimpleUrlHandlerMapping();
        urlHandlerMapping.setUrlMap(socketHandlers);
        urlHandlerMapping.setOrder(1);
        return urlHandlerMapping;
    }

    @Bean
    public WebSocketHandlerAdapter getWebSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
