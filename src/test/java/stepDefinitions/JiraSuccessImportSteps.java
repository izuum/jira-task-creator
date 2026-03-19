package stepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jtc.model.jira.JiraResponse;
import org.junit.jupiter.api.Assertions;

import java.nio.file.Files;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class JiraSuccessImportSteps {
    private final TestContext context;

    public JiraSuccessImportSteps(TestContext context) {
        this.context = context;
    }

    @Given("Используются тестовые учетные данные Jira")
    public void setupJiraProperties() {
        when(context.jiraProperties.getUrl()).thenReturn("https://test.atlassian.net");
        when(context.jiraProperties.getUserName()).thenReturn("testuser");
        when(context.jiraProperties.getToken()).thenReturn("testtoken");
    }

    @And("Задан вид ключа возвращаемый от Jira")
    public void jiraMustReturnKeys() {
        String expectedKey = "TEST-123";
        context.expectedKey = expectedKey;
        JiraResponse mockResponse = new JiraResponse();
        mockResponse.setKey(expectedKey);
        when(context.jiraApiClient.createIssue(any())).thenReturn(mockResponse);
    }

    @Given("^Подготовлен YAML файл с задачами по пути C:\\/Temp$")
    public void preparedDataFile(String yamlContent) throws Exception {
        context.filePath = Files.createTempFile("test", ".yml");
        Files.writeString(context.filePath, yamlContent);
        System.out.println("Cоздан файл: " + context.filePath);
    }

    @When("Запустить программу JiraTaskCreator")
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

    @And("Кол-во созданных в Jira задач: {int}")
    public void shouldCreateNIssuesInJira(int n) {
        Assertions.assertEquals(n, context.importResult.success());
        verify(context.jiraApiClient, times(n)).createIssue(any());
    }

    @And("Jira возвращает ключ(и) задачи вида {string}")
    public void taskMustHaveKey(String pattern) {
        List<String> actualKeys = context.importResult.createdKeys();
        String regex = pattern.replace("*", ".*");
        actualKeys.forEach(key ->
                Assertions.assertTrue(key.matches(regex), "Неверный формат ключа:" + key));
        Assertions.assertEquals(context.importResult.createdKeys().get(0), context.expectedKey);
    }

    @And("Ни одной задачи не должно быть создано")
    public void noTaskShouldBeCreated() {
        Assertions.assertEquals(0, context.importResult.success());
    }
}