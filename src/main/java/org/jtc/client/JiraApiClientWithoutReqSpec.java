package org.jtc.client;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.jtc.config.JiraProperties;
import org.jtc.exceptions.ImportException;
import org.jtc.model.jira.JiraIssue;
import org.jtc.model.jira.JiraResponse;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@Log4j2
public class JiraApiClientWithoutReqSpec {

    private final JiraProperties properties;

    public JiraApiClientWithoutReqSpec(JiraProperties jiraProperties) {
        this.properties = jiraProperties;
        log.info("Jira клиент(без requestSpec) инициализирован для URL: {}", jiraProperties.getUrl());
    }

    @PostConstruct
    public void init() {
        log.info("Инициализация клиента и проверка подключения....");
        testConnection();
    }

    // Создаем заголовок для авторизации
    private String createAuthHeader(){
        String credentials = properties.getUserName() + ":" + properties.getToken();
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        return "Basic " + encodedCredentials;
    }

    public void testConnection(){
        log.info("Тестирование подключения к Jira....");

        try {
            String url = properties.getUrl() + "/rest/api/"
                    + properties.getApi().getVersion() + "/myself";
            log.debug(url);

            // Создаем заголовки
            String authHeader = createAuthHeader();
            log.debug("Auth header создан");

            // Отправляем GET-запрос
            Response response = RestAssured
                    .given()
                    .header("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .when()
                    .get(url)
                    .then()
                    .log().all()
                    .extract().response();

            if(response.getStatusCode() == 200){
                String displayName = response.jsonPath().getString("displayName");
                String accountId = response.jsonPath().getString("accountId");
                log.info("Подключение успшено! Пользователь: {} (ID: {})", displayName, accountId);
            } else {
                log.error("Ошибка подключения. Код: {}, тело {}",
                        response.statusCode(), response.body().asString());
            }
        } catch (Exception ex) {
            log.error("Исключение при подключении к Jira", ex);
        }
    }

    // Создание задачи
    public JiraResponse createIssue(JiraIssue jiraIssue){
        log.info("Отправка запроса на создание задачи");

        try {
            String url = properties.getUrl() + "/rest/api/" + properties.getApi().getVersion() + "/issue";
            log.debug("URL: {}", url);

            String authHeader = createAuthHeader();

            Response response = RestAssured
                    .given()
                    .header("Authorization", authHeader)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .body(jiraIssue)
                    .log().all()
                    .when()
                    .post(url)
                    .then()
                    .log().all()
                    .extract().response();

            if (response.statusCode() == 201){
                JiraResponse jiraResponse = response.as(JiraResponse.class);
                log.info("Задача создана: {}", jiraResponse.getKey());
                return jiraResponse;
            } else if (response.statusCode() == 400){
                log.error("Ошибка валидации. Тело: {}", response.body().asString());
                throw new ImportException("Ошибка валидации: " + response.body().asString());
            } else if (response.statusCode() == 401) {
                log.error("Ошибка авторизации. Проверьте токен.");
                throw new ImportException("Ошибка авторизации");
            } else {
                log.error("Ошибка создания задачи. Код: {}, тело: {}",
                        response.statusCode(), response.body().asString());
                throw new ImportException("Ошибка создания задачи. Код: " + response.statusCode());
            }
        } catch (ImportException e) {
            throw e;
        } catch (Exception e) {
            log.error("Исключение при вызове Jira API", e);
            throw new ImportException("Ошибка при создании задачи: " + e.getMessage(), e);
        }

    }
}
