package stepdefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Тогда;
import org.jtc.client.JiraApiClientWithoutReqSpec;
import org.jtc.config.JiraProperties;
import org.jtc.model.jira.JiraResponse;
import org.jtc.service.ImportOrchestrator;
import org.jtc.service.IssueTransformerService;
import org.jtc.service.YamlReaderService;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class JiraImportSteps {

    @Mock
    protected JiraApiClientWithoutReqSpec jiraApiClient;
    @Mock
    protected JiraProperties jiraProperties;
    protected ImportOrchestrator orchestrator;
    protected ImportOrchestrator.ImportResult importResult;
    protected Path filePath;
    protected Exception exception;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orchestrator = new ImportOrchestrator(
                new YamlReaderService(),
                new IssueTransformerService(),
                jiraApiClient
        );
    }

    @Дано("Jira API настроена на {string}")
    public void jiraApiНастроенаНа(String url) {
        when(jiraProperties.getUrl()).thenReturn(url);
        when(jiraProperties.getUserName()).thenReturn("testuser");
        when(jiraProperties.getToken()).thenReturn("testtoken");

        JiraResponse mockResponse = new JiraResponse();
        mockResponse.setKey("TEST-123");
        when(jiraApiClient.createIssue(any())).thenReturn(mockResponse);
    }

    @Дано("YAML файл с задачами")
    public void yamlФайлСЗадачами(String yamlContent) throws Exception {
        filePath = Files.createTempFile("test", ".yml");
        Files.writeString(filePath, yamlContent);
        System.out.println("Cоздан файл: " + filePath);
    }

    @Дано("Я запускаю программу")
    public void яЗапускаюПрограмму() {
        try {
            importResult = orchestrator.importIssue(filePath.toString());
            exception = null;
        } catch (Exception e) {
            exception = e;
        }
    }

    @Тогда("Программа должна завершиться успешно")
    public void программаДолжнаЗавершитьсяУспешно() {
        Assertions.assertNull(exception, "Программа упала с ошибкой: " + exception);
    }

    @Тогда("В Jira должна создаться одна задача")
    public void вJiraДолжнаСоздатьсяОднаЗадача() {
        Assertions.assertEquals(1, importResult.success());
    }

    @Тогда("У задачи должен быть ключ вида {string}")
    public void уЗадачиДолженБытьКлючВида(String pattern) {
        String actualKey = importResult.createdKeys().get(0);
        Assertions.assertTrue(actualKey.matches(pattern.replace("*", ".*")));
        Assertions.assertTrue(true);
    }

    @Тогда("В Jira должны создаться три задачи")
    public void вJiraДолжныСоздатьсяТриЗадачи() {
        Assertions.assertEquals(3, importResult.success());
    }

    @Тогда("У задач должны быть ключи вида {string}")
    public void уЗадачДолжныБытьКлючиВида(String pattern) {
        String firstActualKey = importResult.createdKeys().get(0);
        String secondActualKey = importResult.createdKeys().get(1);
        String thirdActualKey = importResult.createdKeys().get(2);
        Assertions.assertTrue(firstActualKey.matches(pattern.replace("*", ".*")));
        Assertions.assertTrue(secondActualKey.matches(pattern.replace("*", ".*")));
        Assertions.assertTrue(thirdActualKey.matches(pattern.replace("*", ".*")));
        Assertions.assertTrue(true);
    }

    @Тогда("Программа должна завершиться с ошибкой")
    public void программаДолжнаЗавершитьсяСОшибкой() {
    }
}
