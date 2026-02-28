package org.jtc.model.jira;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jtc.model.jira.typeOfFields.Fields;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JiraIssue {
    private Fields fields;

}
