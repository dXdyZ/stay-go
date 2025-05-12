package com.staygo.userservice.client;

import com.staygo.userservice.config.FeignClintConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "booking-service", configuration = FeignClintConfiguration.class,
        fallback = BookingClientFallback.class
)
public interface BookingClient {
    @GetMapping("/bookings/{id}/exists")
    boolean checkHotelExists(@PathVariable Long id);
}
