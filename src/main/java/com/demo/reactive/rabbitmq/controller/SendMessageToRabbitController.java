package com.demo.reactive.rabbitmq.controller;


import com.demo.reactive.rabbitmq.constant.RabbitMQConstant;
import com.demo.reactive.rabbitmq.dto.MessageDto;
import com.demo.reactive.rabbitmq.producer.MessageSender;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SendMessageToRabbitController {

    MessageSender messageSender;

    @PostMapping(value = "/send", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> sendMessage(@Valid @RequestBody final MessageDto request) {
        messageSender.send(request, RabbitMQConstant.REACTIVE_QUEUE);
        return Mono.just(new ResponseEntity<>(HttpStatus.CREATED));
    }


}
