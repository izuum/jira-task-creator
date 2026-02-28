package org.jtc.model.yaml;

import lombok.Data;

import java.util.List;

// Класс для импорта данных из yml
@Data
public class IssueImport {
    private List<IssueData> issues;
}
