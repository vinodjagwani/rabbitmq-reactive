package com.demo.reactive.config;

import com.demo.reactive.rabbitmq.constant.RabbitMQConstant;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Delivery;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.*;

import javax.annotation.PreDestroy;
import java.util.Objects;


@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RabbitMQConfig {


    @Autowired
    Mono<Connection> connectionMono;

    @Autowired
    AmqpAdmin amqpAdmin;


    @Bean
    public Mono<Connection> connectionMono(final RabbitProperties rabbitProperties) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(rabbitProperties.getHost());
        connectionFactory.setPort(rabbitProperties.getPort());
        connectionFactory.setUsername(rabbitProperties.getUsername());
        connectionFactory.setPassword(rabbitProperties.getPassword());
        return Mono.fromCallable(() -> connectionFactory.newConnection("reactor-rabbit")).cache();
    }

    @Bean
    public Sender sender(final Mono<Connection> connectionMono) {
        return RabbitFlux.createSender((new SenderOptions()).connectionMono(connectionMono));
    }

    @Bean
    public Receiver receiver(final Mono<Connection> connectionMono) {
        return RabbitFlux.createReceiver((new ReceiverOptions()).connectionMono(connectionMono));
    }

    @Bean
    public Flux<Delivery> deliveryFlux(final Receiver receiver) {
        return receiver.consumeAutoAck(RabbitMQConstant.REACTIVE_QUEUE);
    }

    @Bean
    public Queue reactiveQueue() {
        return QueueBuilder.durable(RabbitMQConstant.REACTIVE_QUEUE).build();
    }


    @PreDestroy
    public void close() throws Exception {
        (Objects.requireNonNull(connectionMono.block())).close();
    }
}
