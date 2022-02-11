package models.refactorings;

import java.util.Arrays;
import java.util.List;

public class ExtractedMethodCall extends RefactoringData {

    String[] extractedCodeFragments;

    public ExtractedMethodCall(String[] attributes) {
        super();
        this.startOffset = Long.parseLong(attributes[0]);
        this.endOffset = Long.parseLong(attributes[1]);
        List<String> extractedCodeFragments = Arrays.asList(attributes).subList(2, attributes.length);
        this.extractedCodeFragments = extractedCodeFragments.toArray(new String[0]);
    }

    @Override
    public String renderData() {
        return "<b>EXTRACTED METHOD CALL</b><br>"
                + printAdditionalData();
    }

    public String renderExtractedCodeFragments() {
        StringBuilder result = new StringBuilder();
        int limit = Math.min(extractedCodeFragments.length, 7);
        for (int i = 0; i < limit; i++) {
            result.append(extractedCodeFragments[i]).append("<br>");
        }
        if (extractedCodeFragments.length > 7) {
            result.append("...<br>");
        }
        return result.toString();
    }

    @Override
    public String getType() {
        return "EXTRACTED_METHOD_CALL";
    }
}
