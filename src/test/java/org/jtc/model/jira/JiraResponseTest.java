package org.jtc.model.jira;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JiraResponseTest {

    @Test
    void shouldSetAndGetResponseFields() {
        JiraResponse response = new JiraResponse();

        response.setId("123");
        response.setKey("QL");
        response.setSelf("response.com");

        assertEquals("123", response.getId());
        assertEquals("QL", response.getKey());
        assertEquals("response.com", response.getSelf());
    }
}
