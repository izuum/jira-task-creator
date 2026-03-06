package org.jtc.model.yaml;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class IssueData {
    private String project;
    private String summary;
    @JsonProperty("type")
    private String issueType;
    private String priority;
    private String description;

    public void validateRequiredFields() {
        if (project == null || project.trim().isEmpty()) {
            throw new IllegalArgumentException("Отсутствует ключ проекта (project)");
        }
        if (summary == null || summary.trim().isEmpty()) {
            throw new IllegalArgumentException("Отсутствует краткое описание задачи (summary)");
        }
        if (issueType == null || issueType.trim().isEmpty()) {
            throw new IllegalArgumentException("Отсутствует тип задачи (issueType)");
        }
    }
}
