package org.jtc.model.yaml;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jtc.model.yaml.typeOfFields.Fields;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueData {
    private Fields fields;

}
