package org.jtc.model.jira;

import org.jtc.model.jira.typeOfFields.Fields;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JiraIssueTest {

    @Test
    void shouldSetAndGetFieldsOfJiraIssue(){
        Fields fields = new Fields();
        JiraIssue jiraIssue = new JiraIssue();

        jiraIssue.setFields(fields);

        assertEquals(fields, jiraIssue.getFields());
    }

    @Test
    void shouldCreateJiraIssueViaConstructor(){
        Fields fields = new Fields();
        fields.setSummary("summary");
        JiraIssue jiraIssue = new JiraIssue(fields);

        assertEquals("summary", jiraIssue.getFields().getSummary());
    }
}
