package stepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.jtc.model.jira.JiraResponse;
import org.junit.jupiter.api.Assertions;

import java.nio.file.Files;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class JiraSuccessImportSteps {
    private TestContext context;

    public JiraSuccessImportSteps(TestContext context) {
        this.context = context;
    }

    @Given("Конфигурация Jira загружена из application.yaml")
    public void setupJiraProperties() {
        when(context.jiraProperties.getUrl()).thenReturn("https://test.atlassian.net");
        when(context.jiraProperties.getUserName()).thenReturn("testuser");
        when(context.jiraProperties.getToken()).thenReturn("testtoken");
    }

    @And("Задан вид ключа возвращаемый от Jira")
    public void jiraMustReturnKeys() {
        JiraResponse mockResponse = new JiraResponse();
        mockResponse.setKey("TEST-123");
        when(context.jiraApiClient.createIssue(any())).thenReturn(mockResponse);
    }

    @Given("Подготовлен YAML файл с задачами")
    public void preparedDataFile(String yamlContent) throws Exception {
        context.filePath = Files.createTempFile("test", ".yml");
        Files.writeString(context.filePath, yamlContent);
        System.out.println("Cоздан файл: " + context.filePath);
    }

    @Given("Запускается программа")
    public void startProgram() {
        try {
            context.importResult = context.orchestrator.importIssue(context.filePath.toString());
            context.exception = null;
        } catch (Exception e) {
            context.exception = e;
        }
    }

    @Then("Программа должна завершиться успешно")
    public void successfulCompleteProgram() {
        Assertions.assertNull(context.exception, "Программа упала с ошибкой: " + context.exception);
    }

    @Then("В Jira создается одна задача")
    public void shouldCreateOneIssueInJira() {
        Assertions.assertEquals(1, context.importResult.success());
    }

    @Then("Jira возвращает ключ задачи вида {string}")
    public void taskMustHaveKey(String pattern) {
        String actualKey = context.importResult.createdKeys().get(0);
        Assertions.assertTrue(actualKey.matches(pattern.replace("*", ".*")));
    }

    @Then("В Jira должны создаться три задачи")
    public void shouldCreateThreeIssuesInJira() {
        Assertions.assertEquals(3, context.importResult.success());
    }

    @Then("Jira возвращает ключи задачи вида {string}")
    public void tasksMustHaveKeys(String pattern) {
        String firstActualKey = context.importResult.createdKeys().get(0);
        String secondActualKey = context.importResult.createdKeys().get(1);
        String thirdActualKey = context.importResult.createdKeys().get(2);
        Assertions.assertTrue(firstActualKey.matches(pattern.replace("*", ".*")));
        Assertions.assertTrue(secondActualKey.matches(pattern.replace("*", ".*")));
        Assertions.assertTrue(thirdActualKey.matches(pattern.replace("*", ".*")));
    }

    @And("Ни одной задачи не должно быть создано")
    public void noTaskShouldBeCreated() {
        Assertions.assertEquals(0, context.importResult.success());
    }
}
