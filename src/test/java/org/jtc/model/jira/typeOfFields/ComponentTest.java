package org.jtc.model.jira.typeOfFields;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ComponentTest {

    @Test
    void shouldSetAndGetNameOfComponent() {
        Component component = new Component();

        component.setName("issueType");

        assertEquals("issueType", component.getName());
    }

    @Test
    void shouldCreateComponentWithNameViaConstructor() {
        Component component = new Component("issueType");

        assertEquals("issueType", component.getName());
    }
}
