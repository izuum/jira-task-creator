package org.jtc.model.jira.typeOfFields;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DescriptionTest {

    @Test
    void shouldCreateDescriptionWithText() {

        String testText = "some description";

        Description description = new Description(testText);

        assertNotNull(description.getContent());
        assertEquals(1, description.getContent().size());

        Description.ContentNode paragraph = description.getContent().get(0);
        assertEquals("paragraph", paragraph.getType());
        assertNotNull(paragraph.getContent());
        assertEquals(1, paragraph.getContent().size());

        Description.TextNode textNode = paragraph.getContent().get(0);
        assertEquals("text", textNode.getType());
        assertEquals(testText, textNode.getText());
    }

    @Test
    void shouldCreateDescriptionWithNullText() {
        Description description = new Description(null);

        assertNull(description.getContent().get(0).getContent().get(0).getText());
    }
}
