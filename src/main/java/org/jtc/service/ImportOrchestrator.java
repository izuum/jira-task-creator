package org.jtc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jtc.client.JiraApiClient;
import org.jtc.exceptions.ImportException;
import org.jtc.model.yaml.IssueData;
import org.jtc.model.yaml.IssueImport;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ImportOrchestrator {

    private final YamlReaderService yamlReader;
    private final IssueTransformerService issueTransformer;
    private final JiraApiClient jiraApiClient;

    public record ImportResult(
            int total,
            int success,
            int failed,
            List<String> createdKeys,
            List<String> errors
    ){
        public boolean hasErrors() {
            return failed > 0;
        }
    }

    public ImportResult importIssue(String yamlFilePath){
        log.info("=== Начало импорта ===");

        // Читаем yml и если файл не найден - сразу возвращаем ошибку
        IssueImport issueImport;
        try {
            issueImport = yamlReader.readIssue(yamlFilePath);
        } catch (ImportException e) {
            return new ImportResult(0,0,1,List.of(), List.of(e.getMessage()));
        }

        List<IssueData> issues = issueImport.getIssues();

        // Проверяем есть ли вообще задачи в файле
        if (issues == null || issues.isEmpty()) {
            log.warn("Файл не содержит задач");
            return new ImportResult(0,0,0,List.of(), List.of());
        }

        log.info("Найдено задач для импорта: {}", issues.size());

        // Для статистики сколько задач и для каких проектов
        Map<String, Long> projectsCount = issues.stream()
                .collect(Collectors.groupingBy(
                        IssueData::getProject,
                        Collectors.counting()
                        ));

        log.info("Распределение по проектам:");
        projectsCount.forEach((project, count) ->
                log.info("  {}: {} задач", project, count));
    }
}
