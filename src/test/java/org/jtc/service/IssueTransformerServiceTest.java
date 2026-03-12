package org.jtc.service;

import org.jtc.model.jira.JiraIssue;
import org.jtc.model.jira.typeOfFields.Fields;
import org.jtc.model.yaml.IssueData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IssueTransformerServiceTest {

    private IssueTransformerService transformer;

    @BeforeEach
    void setUp(){
        transformer = new IssueTransformerService();
    }

    @Test
    void shouldTransformRequiredFieldsOnly(){
        IssueData issue = new IssueData();
        issue.setProject("TEST");
        issue.setSummary("some summary");
        issue.setIssueType("Task");

        JiraIssue result = transformer.transformToJiraIssue(issue);

        assertNotNull(result);
        assertNotNull(result.getFields());

        Fields fields = result.getFields();
        assertEquals("TEST", fields.getProject().getKey());
        assertEquals("some summary", fields.getSummary());
        assertEquals("Task", fields.getIssueType().getName());

        assertNull(fields.getPriority());
        assertNull(fields.getDescription());
    }

    @Test
    void shouldTransformAllFields() {
        IssueData issue = new IssueData();
        issue.setProject("TEST");
        issue.setSummary("some summary");
        issue.setIssueType("Task");
        issue.setPriority("1");
        issue.setDescription("some description");

        JiraIssue result = transformer.transformToJiraIssue(issue);

        assertNotNull(result);
        assertNotNull(result.getFields());

        Fields fields = result.getFields();

        assertEquals("1", fields.getPriority().getName());
        assertNotNull(fields.getDescription());
        assertEquals("some description",
                fields.getDescription().getContent().get(0).getContent().get(0).getText());
    }

    @Test
    void shouldThrowExceptionWhenProjectIsMissing(){
        IssueData issue = new IssueData();
        issue.setSummary("without project");
        issue.setIssueType("Task");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> transformer.transformToJiraIssue(issue));

        assertTrue(exception.getMessage().toLowerCase().contains("проект"));
    }

    @Test
    void shouldThrowExceptionWhenSummaryIsMissing(){
        IssueData issue = new IssueData();
        issue.setProject("TEST without summary");
        issue.setIssueType("Bug");

        assertThrows(IllegalArgumentException.class,
                () -> transformer.transformToJiraIssue(issue));
    }

    @Test
    void shouldThrowExceptionWhenIssueTypeIsMissing(){
        IssueData issue = new IssueData();
        issue.setProject("TEST");
        issue.setSummary("without issuetype");

        assertThrows(IllegalArgumentException.class,
                () -> transformer.transformToJiraIssue(issue));
    }
}
