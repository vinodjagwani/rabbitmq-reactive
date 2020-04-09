package com.demo.reactive;


import com.demo.reactive.rabbitmq.consumer.MessageReceiver;
import com.demo.reactive.rabbitmq.dto.MessageDto;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import static com.demo.reactive.rabbitmq.constant.RabbitMQConstant.REACTIVE_QUEUE;
import static org.junit.jupiter.api.Assertions.assertEquals;


@FieldDefaults(level = AccessLevel.PRIVATE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebClientTest {

    @LocalServerPort
    int port;

    @Autowired
    MessageReceiver messageReceiver;

    @Test
    @Order(1)
    public void testSendMessageToRabbitMQ() {
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage("test-test1");
        WebTestClient client = WebTestClient.bindToServer().baseUrl("http://localhost:" + port).build();
        client.post()
                .uri("/send")
                .body(BodyInserters.fromValue(messageDto))
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    @Order(2)
    public void testReceiveMessageFromRabbitMQ() {
        MessageDto messageDto = new MessageDto();
        messageDto.setMessage("test-test1");
        messageReceiver.consume(REACTIVE_QUEUE).subscribe(s -> assertEquals(messageDto.toString(), new String((byte[]) s)));
    }

}
