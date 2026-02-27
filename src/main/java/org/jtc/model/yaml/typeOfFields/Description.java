package org.jtc.model.yaml.typeOfFields;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Description {
    private String type = "doc";
    private int version = 1;
    private List<ContentNode> content;

    public Description(String plainText){
        TextNode textNode = new TextNode("text", plainText);
        ContentNode paragraph = new ContentNode("paragraph", Collections.singletonList(textNode));
        this.content = Collections.singletonList(paragraph);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContentNode{
        private String type;
        private List<TextNode> content;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TextNode{
        private String type;
        private String text;
    }
}
