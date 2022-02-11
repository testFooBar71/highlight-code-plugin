package models.refactorings;

import java.util.Arrays;
import java.util.List;

public class ExtractInterface extends RefactoringData {
    String[] subclasses;

    public ExtractInterface(String... attributes) {
        super();
        this.startOffset = Long.parseLong(attributes[0]);
        this.endOffset = Long.parseLong(attributes[1]);
        List<String> subclasses = Arrays.asList(attributes).subList(2, attributes.length);
        this.subclasses = subclasses.toArray(new String[0]);
    }

    @Override
    public String renderData() {
        return "<b>EXTRACTED INTERFACE<br>Classes that implement:</b><br>"
                + getSubclasses() + printAdditionalData();
    }

    private String getSubclasses() {
        StringBuilder result = new StringBuilder();
        for (String parameter: subclasses) {
            result.append(parameter).append("<br>");
        }
        return result.toString();
    }

    @Override
    public String getType() {
        return "EXTRACT_INTERFACE";
    }
}
