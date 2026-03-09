package org.jtc.model.jira.typeOfFields;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FieldsTest {

    @Test
    void shouldSetAndGetFields() {
        Fields fields = new Fields();

        Project project = new Project();
        project.setKey("TEST");
        fields.setProject(project);
        fields.setSummary("some summary");

        Component issueType = new Component();
        issueType.setName("issueType");
        fields.setIssueType(issueType);

        Component priority = new Component();
        priority.setName("priority");
        fields.setPriority(priority);

        Description description = new Description("some description");
        fields.setDescription(description);

        assertEquals("TEST", fields.getProject().getKey());
        assertEquals("some summary", fields.getSummary());
        assertEquals("issueType", fields.getIssueType().getName());
        assertEquals("priority", fields.getPriority().getName());

        assertEquals("some description",
                description.getContent().get(0).getContent().get(0).getText());
    }
}
