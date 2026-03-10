package org.jtc.model.yaml;

import org.jtc.model.yaml.IssueData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IssueDataTest {

    private IssueData issue;

    @BeforeEach
    public void setUp() {
        issue =  new IssueData();
        issue.setProject("TEST");
        issue.setSummary("some summary");
        issue.setIssueType("Bug");
        issue.setPriority("High");
        issue.setDescription("some description");
    }
    @Test
    void shouldSetAndGetIssueDataFields() {
        assertEquals("TEST", issue.getProject());
        assertEquals("some summary", issue.getSummary());
        assertEquals("Bug", issue.getIssueType());
        assertEquals("High", issue.getPriority());
        assertEquals("some description", issue.getDescription());
    }

    @Test
    void shouldThrowExceptionWhenProjectIsEmpty() {
        issue.setProject("");
        IllegalArgumentException ex =  assertThrows(IllegalArgumentException.class, () -> {
            issue.validateRequiredFields();
        });
    }

    @Test
    void shouldThrowExceptionWhenProjectIsNull() {
        issue.setProject(null);
        IllegalArgumentException ex =  assertThrows(IllegalArgumentException.class, () -> {
            issue.validateRequiredFields();
        });
    }

    @Test
    void shouldThrowExceptionWhenSummaryIsEmpty() {
        issue.setSummary("");
        IllegalArgumentException ex =  assertThrows(IllegalArgumentException.class, () -> {
            issue.validateRequiredFields();
        });
    }

    @Test
    void shouldThrowExceptionWhenSummaryIsNull() {
        issue.setSummary(null);
        IllegalArgumentException ex =  assertThrows(IllegalArgumentException.class, () -> {
            issue.validateRequiredFields();
        });
    }

    @Test
    void shouldThrowExceptionWhenIssueTypeIsEmpty() {
        issue.setIssueType("");
        IllegalArgumentException ex =  assertThrows(IllegalArgumentException.class, () -> {
            issue.validateRequiredFields();
        });
    }

    @Test
    void shouldThrowExceptionWhenIssueTypeIsNull() {
        issue.setIssueType(null);
        IllegalArgumentException ex =  assertThrows(IllegalArgumentException.class, () -> {
            issue.validateRequiredFields();
        });
    }
}