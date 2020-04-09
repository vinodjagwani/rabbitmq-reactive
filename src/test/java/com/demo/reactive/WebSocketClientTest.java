package com.demo.reactive;


import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@FieldDefaults(level = AccessLevel.PRIVATE)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketClientTest {

    @LocalServerPort
    int port;

    @Test
    public void testSendAndReceiveMessageWebSocket() {
        WebSocketClient client = new ReactorNettyWebSocketClient();
        Assertions.assertThrows(IllegalStateException.class, () ->
                client.execute(URI.create("ws://localhost:" + port + "/send-websocket"),
                        session -> session.send(Mono.just(session.textMessage("websocket-testing")))
                                .thenMany(session.receive()
                                        .map(WebSocketMessage::getPayloadAsText)
                                        .log()).doOnNext(b -> assertEquals("Received from server :: websocket-testing", b))
                                .then()).block(Duration.ofSeconds(1)));
    }
}
