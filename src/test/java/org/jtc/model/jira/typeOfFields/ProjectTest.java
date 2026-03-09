package org.jtc.model.jira.typeOfFields;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProjectTest {

    @Test
    void shouldSetAndGetKey(){
        Project project = new Project();
        project.setKey("TEST");

        assertEquals("TEST", project.getKey());
    }

    @Test
    void shouldCreateProjectWithKeyViaConstructor(){
        Project project = new Project("TEST");

        assertEquals("TEST", project.getKey());
    }
}
