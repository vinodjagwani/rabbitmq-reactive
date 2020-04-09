package com.demo.reactive.websocket.handler.impl;


import com.demo.reactive.websocket.handler.WebSocketHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
public class ReactiveWebSocketHandler implements WebSocketHandler {


    @Override
    public Mono<Void> handle(final WebSocketSession webSocketSession) {
        return webSocketSession.send(webSocketSession.receive()
                .map(message -> "Received from server :: " + message.getPayloadAsText())
                .map(webSocketSession::textMessage).log());
    }
}
