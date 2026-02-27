package org.jtc.model.yaml.typeOfFields;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jtc.config.JiraProperties;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Fields {
    private JiraProperties.Project project;
    private String summary;
    private Component issueType;
    private Description description;
    private Component priority;
}
