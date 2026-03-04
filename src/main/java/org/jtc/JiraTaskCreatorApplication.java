package org.jtc;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jtc.service.ImportOrchestrator;
import org.jtc.service.ImportOrchestrator.ImportResult;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
@Log4j2
@RequiredArgsConstructor
public class JiraTaskCreatorApplication implements CommandLineRunner {

    private final ImportOrchestrator importOrchestrator;

    public static void main(String[] args) {
        SpringApplication.run(JiraTaskCreatorApplication.class, new String[]{"src/main/resources/issues.yml"});
    }

    @Override
    public void run(String... args) throws Exception {
        if(args.length == 0){
            log.error("Не указан путь к YAML файлу");
            System.exit(1);
        }

        String yamlFile = args[0];
        log.info("Файл с задачами {}", yamlFile);
        ImportResult result = importOrchestrator.importIssue(yamlFile);

        if(result.hasErrors()){
            System.exit(1);
        } else {
            System.exit(0);
        }

    }
}