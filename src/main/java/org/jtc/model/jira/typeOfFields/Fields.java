package org.jtc.model.jira.typeOfFields;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Fields {
    private Project project;
    private String summary;
    private Component issueType;
    private Component priority;
    private Description description;
}
