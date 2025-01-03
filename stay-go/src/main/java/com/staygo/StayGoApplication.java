package com.staygo;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class StayGoApplication {

    private final RabbitAdmin rabbitAdmin;
    private final Queue queueRoom;
    private final Queue queueCar;
    private final Queue queueCode;
    private final Queue queueUserFindHotel;

    public StayGoApplication(RabbitAdmin rabbitAdmin,
                             @Qualifier("roomBookingQueue") Queue queueRoom,
                             @Qualifier("messageCarReservation") Queue queueCar,
                             @Qualifier("massageAboutCodeForUser") Queue queueCode,
                             @Qualifier("findUserHotel") Queue queueUserFindHotel) {
        this.rabbitAdmin = rabbitAdmin;
        this.queueRoom = queueRoom;
        this.queueCar = queueCar;
        this.queueCode = queueCode;
        this.queueUserFindHotel = queueUserFindHotel;
    }


    @PostConstruct
    public void declareQueue() {
        rabbitAdmin.declareQueue(queueCar);
        rabbitAdmin.declareQueue(queueCode);
        rabbitAdmin.declareQueue(queueRoom);
        rabbitAdmin.declareQueue(queueUserFindHotel);
    }

    public static void main(String[] args) {
        SpringApplication.run(StayGoApplication.class, args);
    }

}
