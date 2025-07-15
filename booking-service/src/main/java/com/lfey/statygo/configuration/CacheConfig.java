package com.lfey.statygo.configuration;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder -> {
            //1. Создание базовой конфигурации для сериализации используя JSON
            RedisCacheConfiguration baseConfig = RedisCacheConfiguration.defaultCacheConfig()
                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                            new GenericJackson2JsonRedisSerializer()
                    ));

            builder
                    .withCacheConfiguration("hotelReviewsCount", baseConfig.entryTtl(Duration.ofHours(12))
                    )
                    // Для всего кеша который не был тут указан
                    .cacheDefaults(
                            baseConfig.entryTtl(Duration.ofMillis(20))
                    );
        });
    }
}
