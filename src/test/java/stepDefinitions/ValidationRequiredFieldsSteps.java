package stepDefinitions;

import io.cucumber.java.en.Then;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ValidationRequiredFieldsSteps {
    private final TestContext context;

    public ValidationRequiredFieldsSteps(TestContext context) {
        this.context = context;
    }

    @Then("Программа должна выбросить ошибку с сообщением об отсутствии поля {string}")
    public void verifyMissingFieldsError(String fieldName) {
        assertNotNull(context.importResult);
        assertTrue(context.importResult.hasErrors());

        Map<String, String> expectedMessages = Map.of(
                "project", "Отсутствует ключ проекта (project)",
                "summary", "Отсутствует краткое описание задачи (summary)",
                "type", "Отсутствует тип задачи (issueType)"
        );

        String expectedMessage = expectedMessages.getOrDefault(fieldName,
                String.format("Отсутствует поле %s", fieldName));

        boolean found = context.importResult.errors().stream().anyMatch(error -> error.contains(expectedMessage));

        assertTrue(found, String.format("Ожидалось сообщение: '%s'\nПолученные ошибки: %s",
                expectedMessage, context.importResult.errors()));
    }
}
