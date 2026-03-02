package org.jtc.service;

import lombok.extern.log4j.Log4j2;
import org.jtc.model.jira.JiraIssue;
import org.jtc.model.jira.typeOfFields.Component;
import org.jtc.model.jira.typeOfFields.Description;
import org.jtc.model.jira.typeOfFields.Fields;
import org.jtc.model.jira.typeOfFields.Project;
import org.jtc.model.yaml.IssueData;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class IssueTransformerService {
    public JiraIssue transformToJiraIssue(IssueData issue) {
        log.debug("Преобразование задачи: {}", issue.getSummary());

        if (issue.getProject() == null || issue.getProject().isEmpty()) {
            throw new IllegalArgumentException("У задачи не указан проект: " + issue.getProject());
        }

        Project project = new Project(issue.getProject());
        Component issueType = new Component(issue.getIssueType());

        Fields.FieldsBuilder fieldsBuilder = Fields.builder()
                .project(project)
                .summary(issue.getSummary())
                .issueType(issueType);

        if (issue.getDescription() != null && !issue.getDescription().isEmpty()) {
            fieldsBuilder.description(new Description(issue.getDescription()));
        }
        if (issue.getPriority() != null && !issue.getPriority().isEmpty()) {
            fieldsBuilder.priority(new Component(issue.getPriority()));
        }

        Fields fields = fieldsBuilder.build();
        return new JiraIssue(fields);
    }

    public void validateRequiredFieldsOfIssue(IssueData issue) {
        issue.validateRequiredFields();
    }
}
