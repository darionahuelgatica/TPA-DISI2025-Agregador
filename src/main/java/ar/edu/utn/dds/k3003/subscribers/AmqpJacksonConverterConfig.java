package ar.edu.utn.dds.k3003.subscribers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpJacksonConverterConfig {

    @Bean
    public Jackson2JsonMessageConverter amqpJacksonMessageConverter() {
        var mapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(mapper);
    }
}
