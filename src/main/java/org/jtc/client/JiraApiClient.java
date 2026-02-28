package org.jtc.client;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
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
public class JiraApiClient {

    private final JiraProperties properties;
    private RequestSpecification requestSpec;

    public JiraApiClient(JiraProperties properties) {
        this.properties = properties;
    }

    // создаем конфигурацию подключения к jira
    @PostConstruct
    public void init() {
        RestAssured.baseURI = properties.getUrl();

        // устанавливаем время в течении которого программа будет пытаться подключиться к jira
        // устанавливаем время в течении которго программа будет пытаться отправить запрос в jira
        RestAssured.config = RestAssured.config()
                .httpClient(RestAssured.config().getHttpClientConfig()
                        .setParam("http.connection.timeout", properties.getApi().getConnectTimeout())
                        .setParam("http.socket.timeout", properties.getApi().getReadTimeout()));

        // создаем шаблон для запроса в jira
        this.requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", createAuthHeader())
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        log.info("Jira клиент инициализирован для URL: {}", properties.getUrl());
        testConnection();
    }

    // метод для генерации заголовка с именем и токеном
    private String createAuthHeader() {
        String credentials = properties.getUserName() + ":" + properties.getToken();
        String encoded = Base64.getEncoder().encodeToString(credentials.getBytes());
        return "Basic " + encoded;
    }

    private void testConnection() {
        try {

            String endpoint = String.format("/rest/api/%s/myself", properties.getApi().getVersion());
            Response response = RestAssured
                    .given()
                    .spec(requestSpec)
                    .when()
                    .get(endpoint)
                    .then()
                    .extract().response();

            if (response.getStatusCode() == 200) {
                String displayName = response.jsonPath().getString("displayName");
                log.info("Подключение успешно! Пользователь: {}", displayName);
            } else {
                log.error("Ошибка подключения. Код: {}, тело: {}",
                        response.statusCode(), response.body().asString());
            }
        } catch (Exception e) {
            log.error("Не удалось подключиться к Jira", e);
        }
    }

    public JiraResponse createIssue(JiraIssue jiraIssue) {
        log.info("Отправка запроса на создание задачи");

        String endpoint = String.format("/rest/api/%s/issue", properties.getApi().getVersion());
        try {
            Response response = RestAssured
                    .given()
                    .spec(requestSpec)
                    .body(jiraIssue)
                    .when()
                    .post(endpoint)
                    .then()
                    .extract().response();

            if(response.getStatusCode() == 201) {
                JiraResponse jiraResponse = response.as(JiraResponse.class);
                log.info("Задача создана: {}", jiraResponse.getKey());
                return jiraResponse;
            } else {
                log.error("Ошибка создания задачи. Код: {}, тело: {}",
                        response.statusCode(), response.body().asString());
                throw new ImportException("Ошбика создания задачи. Код:  " + response.statusCode());
            }
        } catch (Exception e) {
            log.error("Исключение при вызове Jira API", e);
            throw new ImportException("Ошибка создания задачи", e);
        }
    }

}
