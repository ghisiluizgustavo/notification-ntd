package me.ghisiluizgustavo.notification.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI notificationOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Notification System API")
                .description("API for managing multi-channel notifications (EMAIL, SMS, PUSH) based on user subscriptions and categories")
                .version("1.0.0")
                .contact(new Contact()
                    .name("API Support")
                    .email("support@notification-system.com")))
            .servers(List.of(
                new Server()
                    .url("http://localhost:8080")
                    .description("Local development server")
            ));
    }
}
