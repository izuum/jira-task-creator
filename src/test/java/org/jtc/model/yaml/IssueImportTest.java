package org.jtc.model.yaml;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class IssueImportTest {

    @Test
    void shouldSetAndGetListOfIssue() {
        IssueData issue = new IssueData();
        issue.setProject("QL");
        issue.setSummary("qweqweqwe");
        issue.setIssueType("Task");

        List<IssueData> listOfIssue = new ArrayList<>();
        listOfIssue.add(issue);
        listOfIssue.add(issue);

        IssueImport issueImport = new IssueImport();
        issueImport.setIssues(listOfIssue);

        assertEquals(2, issueImport.getIssues().size());
        assertEquals("QL", issueImport.getIssues().get(0).getProject());
        assertEquals("qweqweqwe", issueImport.getIssues().get(0).getSummary());
        assertEquals("Task", issueImport.getIssues().get(0).getIssueType());

        assertEquals("QL", issueImport.getIssues().get(1).getProject());
        assertEquals("qweqweqwe", issueImport.getIssues().get(1).getSummary());
        assertEquals("Task", issueImport.getIssues().get(1).getIssueType());
    }

    @Test
    void shouldCreateWithEmptyList() {
        IssueImport issueImport = new IssueImport();
        issueImport.setIssues(new ArrayList<>());

        assertNotNull(issueImport.getIssues());
        assertTrue(issueImport.getIssues().isEmpty());
    }

    @Test
    void shouldCreateWithNullList(){
        IssueImport issueImport = new IssueImport();
        issueImport.setIssues(null);

        assertNull(issueImport.getIssues());
    }
}
