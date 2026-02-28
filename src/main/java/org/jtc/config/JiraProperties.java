package org.jtc.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "jira")
@Data
@Validated
public class JiraProperties {

    @NotBlank(message = "URL Jira обязателен")
    private String url;

    @NotBlank(message = "Имя пользователя обязательно")
    private String userName;

    @NotBlank(message = "Токен обязателен")
    private String token;

    private Api api = new Api();


    @Data
    public static class Api{
        private String version = "3";
        private int connectTimeout = 10000;
        private int readTimeout = 30000;
    }
}
