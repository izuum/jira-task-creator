package org.jtc.service;

import org.jtc.exceptions.ImportException;
import org.jtc.model.yaml.IssueData;
import org.jtc.model.yaml.IssueImport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class YamlReaderServiceTest {

    private YamlReaderService yamlReaderService;

    @BeforeEach
    public void setUp() {
        yamlReaderService = new YamlReaderService();
    }

    @Test
    void shouldReadValidYamlFile(@TempDir Path tempDir) throws IOException {
        Path yamlFile = tempDir.resolve("issue.yml");
        String yamlContent = """
                issues:
                  - project: "TEST"
                    summary: "Check your tests"
                    type: "Task"
                    priority: "1"
                    description: "Check your tests for valid"
                """;
        Files.writeString(yamlFile, yamlContent);

        IssueImport result = yamlReaderService.readIssue(yamlFile.toString());

        assertNotNull(result);
        assertNotNull(result.getIssues());
        assertEquals(1, result.getIssues().size());

        IssueData issueData = result.getIssues().get(0);
        assertEquals("TEST", issueData.getProject());
        assertEquals("Check your tests", issueData.getSummary());
        assertEquals("Task", issueData.getIssueType());
        assertEquals("1", issueData.getPriority());
        assertEquals("Check your tests for valid", issueData.getDescription());
    }

    @Test
    void shouldThrownExceptionWhenFileNotFound(){
        String nonExistentFile = "non-existent-file.yml";

        ImportException exception = assertThrows(ImportException.class, () ->
                yamlReaderService.readIssue(nonExistentFile));

        assertTrue(exception.getMessage().contains("Файл не найден"));
    }
}
