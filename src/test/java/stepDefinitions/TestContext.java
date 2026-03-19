package stepDefinitions;

import org.jtc.client.JiraApiClientWithoutReqSpec;
import org.jtc.config.JiraProperties;
import org.jtc.model.jira.JiraResponse;
import org.jtc.service.ImportOrchestrator;
import org.jtc.service.IssueTransformerService;
import org.jtc.service.YamlReaderService;

import java.nio.file.Path;

import static org.mockito.Mockito.mock;

public class TestContext {
    public final JiraApiClientWithoutReqSpec jiraApiClient;
    public final JiraProperties jiraProperties;
    public final YamlReaderService yamlReader;
    public final IssueTransformerService issueTransformer;
    public final ImportOrchestrator orchestrator;
    public ImportOrchestrator.ImportResult importResult;
    public JiraResponse jiraResponse;
    public Path filePath;
    public Exception exception;

    public TestContext(){
        this.jiraApiClient = mock(JiraApiClientWithoutReqSpec.class);
        this.jiraProperties = mock(JiraProperties.class);
        this.yamlReader = new YamlReaderService();
        this.issueTransformer = new IssueTransformerService();
        this.jiraResponse = new JiraResponse();

        this.orchestrator = new ImportOrchestrator(
                this.yamlReader,
                this.issueTransformer,
                this.jiraApiClient
        );
    }

}
