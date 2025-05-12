package com.lfey.authservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

//Настройка CORS
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
         // Создаем объект конфигурации CORS
        CorsConfiguration configuration = new CorsConfiguration();

        // Разрешенные источники (origins)
        // Важно: localhost и 127.0.0.1 считаются разными источниками в CORS!
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:8080", // Локальный фронтенд
            "http://127.0.0.1:8080"  // Альтернативный адрес локального фронтенда
        ));

        // Разрешенные HTTP-методы
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Разрешенные заголовки запросов
        configuration.setAllowedHeaders(Arrays.asList("*")); // Все заголовки

        // Заголовки, которые будут доступны клиенту
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization", // Чтобы клиент мог получить JWT-токен из заголовка
                "Content-type"
        ));

        // Разрешаем передачу учетных данных (куки, авторизация)
        configuration.setAllowCredentials(true);

        // Настраиваем правила CORS для конкретных URL-путей
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(
            "/api/**",      // Применять правила ко всем URL, начинающимся с /api
            configuration    // Конфигурация CORS
        );

        return source;
    }
}
