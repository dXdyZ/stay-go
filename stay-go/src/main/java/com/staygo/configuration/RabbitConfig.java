package com.staygo.configuration;

import lombok.Setter;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Setter
@Configuration
public class RabbitConfig {

    @Value("${queue.name.booking}")
    private String messageAboutBooking;

    @Value("${queue.name.verefEmail}")
    private String massageAboutCodeForUser;

    @Value("${queue.name.carDataEmail}")
    private String messageCarReservation;

    @Value("${queue.name.userFindHotel}")
    private String userFindHotel;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Bean
    public Queue findUserHotel() {
        return new Queue(userFindHotel, false);
    }

    /**
     * @return Очередь
     */
    @Bean
    public Queue roomBookingQueue() {
        return new Queue(messageAboutBooking, false);
    }

    @Bean
    public Queue massageAboutCodeForUser() {
        return new Queue(massageAboutCodeForUser, false);
    }

    @Bean
    public Queue messageCarReservation() {
        return new Queue(messageCarReservation, false);
    }

    /**
     * Бин создает connection
     * @return ConnectionFactory
     */
    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }

    /**
     * @return Нужен для декларачии очередей
     */
    @Bean
    public RabbitAdmin rabbitAdmin() {
        return new RabbitAdmin(connectionFactory());
    }
}
