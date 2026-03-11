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
    void shouldThrowExceptionWhenFileNotFound(){
        String nonExistentFile = "non-existent-file.yml";

        ImportException exception = assertThrows(ImportException.class, () ->
                yamlReaderService.readIssue(nonExistentFile));

        assertTrue(exception.getMessage().contains("Файл не найден"));
    }

    @Test
    void shouldHandleEmptyFile(@TempDir Path tempDir) throws IOException {

        Path yamlFile = tempDir.resolve("issues.yaml");
        Files.writeString(yamlFile, "issues: []");

        IssueImport result = yamlReaderService.readIssue(yamlFile.toString());

        assertNotNull(result);
        assertNotNull(result.getIssues());
        assertTrue(result.getIssues().isEmpty());
    }

    @Test
    void shouldIgnoreUnknownFields(@TempDir Path tempDir) throws IOException {
        Path yamlFile = tempDir.resolve("issues.yaml");
        String yamlContent = """
                issues:
                  - project: "TEST"
                    summary: "some summary"
                    type: "Task"
                    priority: "1"
                    description: "some description"
                    unknownField: "should be ignore"
                """;

        Files.writeString(yamlFile, yamlContent);

        IssueImport result = yamlReaderService.readIssue(yamlFile.toString());
        assertNotNull(result);
        assertEquals(1, result.getIssues().size());

        IssueData issue = result.getIssues().get(0);
        assertEquals("TEST", issue.getProject());
        assertEquals("some summary", issue.getSummary());
        assertEquals("Task", issue.getIssueType());
        assertEquals("1", issue.getPriority());
        assertEquals("some description", issue.getDescription());
    }

    @Test
    void shouldReadMultipleIssues(@TempDir Path tempDir) throws IOException {
        Path yamlFile = tempDir.resolve("issues.yaml");
        String yamlContent = """
                issues:
                  - project: "TEST1"
                    summary: "some summary"
                    type: "Task"
                  - project: "TEST2"
                    summary: "other summary"
                    type: "Bug"
                  - project: "TEST3"
                    summary: "another summary"
                    type: "Function"
                """;
        Files.writeString(yamlFile, yamlContent);

        IssueImport result = yamlReaderService.readIssue(yamlFile.toString());

        assertEquals(3, result.getIssues().size());
        assertEquals("TEST1", result.getIssues().get(0).getProject());
        assertEquals("TEST2", result.getIssues().get(1).getProject());
        assertEquals("TEST3", result.getIssues().get(2).getProject());
    }

    @Test
    void shouldThrowExceptionWhenPathIsEmpty(){
        assertThrows(ImportException.class, () -> yamlReaderService.readIssue(""));
    }
}
