package ar.edu.utn.dds.k3003.subscribers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfig {

    @Bean
    public org.springframework.amqp.core.Queue hechosQueue(
            @Value("${app.hechos.updates.queue}") String name) {
        return org.springframework.amqp.core.QueueBuilder.durable(name).build();
    }
}