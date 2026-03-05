package org.jtc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jtc.client.JiraApiClient;
import org.jtc.client.JiraApiClientWithoutReqSpec;
import org.jtc.exceptions.ImportException;
import org.jtc.model.jira.JiraIssue;
import org.jtc.model.jira.JiraResponse;
import org.jtc.model.yaml.IssueData;
import org.jtc.model.yaml.IssueImport;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ImportOrchestrator {

    private final YamlReaderService yamlReader;
    private final IssueTransformerService issueTransformer;
//    private final JiraApiClient jiraApiClient;
    private final JiraApiClientWithoutReqSpec jiraApiClient;

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

        List<String> createdKeys = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        // Цикл по всем задачам
        for(int i = 0; i < issues.size(); i++) {
            IssueData issue = issues.get(i);

            log.info("Импорт Задачи {}/{}: [{}] {}",
                    i+1, issues.size(), issue.getProject(), issue.getSummary());
            try {
                issueTransformer.validateRequiredFieldsOfIssue(issue);

                JiraIssue jiraIssue = issueTransformer.transformToJiraIssue(issue);

                JiraResponse response = jiraApiClient.createIssue(jiraIssue);

                createdKeys.add(response.getKey());
                log.info("Задача создана в проекте {}: {}",
                        issue.getProject(), response.getKey());

            } catch (IllegalArgumentException e){
                // Ошибка валидации - не хватает полей
                log.error("Ошибка валидации: {}", e.getMessage());
                errors.add(String.format("[%s] %s: %s",
                        issue.getProject(), issue.getSummary(), e.getMessage()));
            } catch (ImportException e) {
                // Ошибка Jira API - проблема при отправке
                log.error("Ошибка Jira API: {}", e.getMessage());
                errors.add(String.format("[%s] %s: %s",
                        issue.getProject(), issue.getSummary(), e.getMessage()));
            } catch (Exception e) {
                log.error("Непредвиденная ошибка", e);
                errors.add(String.format("[%s] %s: %s",
                        issue.getProject(), issue.getSummary(), e.getMessage()));
            }
        }
        // Для статистики группируем созданные задачи
        Map<String, Long> successByProject = createdKeys.stream()
                .collect(Collectors.groupingBy(
                        key -> key.split("-")[0],
                        Collectors.counting()
                ));

        ImportResult  result = new ImportResult(
                issues.size(),
                createdKeys.size(),
                errors.size(),
                createdKeys,
                errors
        );

        log.info("=== Импорт завершен ===");
        log.info("Всего задач: {}, Успешно: {}, Ошибок: {}",
                result.total(), result.success(), result.failed());

        // Если есть успешные задачи - показываем статистику по проектам
        if(!successByProject.isEmpty()) {
            log.info("Успешно создано по проектам:");
            successByProject.forEach((project, count) ->
                    log.info(" {}: {} задач", project, count));
        }

        // Если есть ошибки - показываем список ошибок
        if (!result.errors().isEmpty()) {
            log.warn("Список ошибок:");
            result.errors().forEach(error -> log.warn(" - {}", error));
        }

        return result;
    }
}
