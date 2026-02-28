package org.jtc.model.yaml;

import lombok.Data;

@Data
public class IssueData {
    private String summary;
    private String issueType;
    private String priority;
    private String description;
}
