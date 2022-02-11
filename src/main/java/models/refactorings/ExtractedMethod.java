package models.refactorings;

import java.util.Arrays;
import java.util.List;

public class ExtractedMethod extends RefactoringData {

    String[] extractedCodeFragments;
    long startOffsetOfSourceOperation;

    long endOffsetOfSourceOperation;

    public ExtractedMethod(String... attributes) {
        super();
        this.startOffset = Long.parseLong(attributes[0]);
        this.endOffset = Long.parseLong(attributes[1]);
        this.startOffsetOfSourceOperation = Long.parseLong(attributes[2]);
        this.endOffsetOfSourceOperation = Long.parseLong(attributes[3]);
        List<String> extractedCodeFragments = Arrays.asList(attributes).subList(4, attributes.length);
        this.extractedCodeFragments = extractedCodeFragments.toArray(new String[0]);
    }

    @Override
    public String renderData() {
        return "<b>EXTRACTED METHOD</b><br>"
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

    public long getStartOffsetOfSourceOperation() {
        return startOffsetOfSourceOperation;
    }

    public long getEndOffsetOfSourceOperation() {
        return endOffsetOfSourceOperation;
    }

    @Override
    public String getType() {
        return "EXTRACTED_METHOD";
    }


}
