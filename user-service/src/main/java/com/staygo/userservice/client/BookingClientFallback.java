package com.staygo.userservice.client;

import com.staygo.userservice.exception.ServerErrorException;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class BookingClientFallback implements BookingClient {
    private final CacheManager cacheManager;

    public BookingClientFallback(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public boolean checkHotelExists(Long id) {
        Cache cache = cacheManager.getCache("hotelExistence");
        if (cache != null) {
            Cache.ValueWrapper wrapper = cache.get(id);
            if (wrapper != null) {
                return (boolean) wrapper.get();
            }
        }
        throw new ServerErrorException("Server error");
    }
}
