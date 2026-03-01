package org.jtc.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.log4j.Log4j2;
import org.jtc.exceptions.ImportException;
import org.jtc.model.yaml.IssueImport;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
@Log4j2
public class YamlReaderService {
    private final ObjectMapper yamlMapper;

    public YamlReaderService(){
        this.yamlMapper = new ObjectMapper(new YAMLFactory());
        // настройка маппера, которая позволяет пропускать неизвестные поля, без риска выброса исключения
        this.yamlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        log.info("YAML маппер инициализирован");
    }

    public IssueImport readIssue(String filePath){
        log.info("Чтение файла с задачами: {}", filePath);

        File file = new File(filePath);
        if(!file.exists()){
            log.error("Файл не найден: {}", filePath);
            throw new ImportException("Файл не найден: "  + filePath);
        }

        try {
            // парсим файл с задачами в list IssueImport
            IssueImport issueImport = yamlMapper.readValue(file, IssueImport.class);
            int countOfIssue = issueImport.getIssues() != null ? issueImport.getIssues().size() : 0;
            log.info("Прочитано {} задач из файла", countOfIssue);
            return issueImport;
        } catch (IOException e) {
            log.error("Ошибка чтения YAML файла: {}", e.getMessage());
            throw new ImportException("Ошибка чтения YAML файла: " + e.getMessage(), e);
        }
    }
}