package App.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // Разрешить для всех запросов с путями, начинающимися на "/api/"
                .allowedOrigins("http://localhost:5173")  // Указывайте адрес фронтенда (поменяли на 5173)
                .allowedMethods("GET", "POST", "PUT", "DELETE","OPTIONS")  // Разрешенные методы
                .allowedHeaders("*");  // Разрешенные заголовки
    }
}

