package models.refactorings;

public class ExtractedVariableUsage extends RefactoringData {

    String extractedCode;
    String replacedBy;

    public ExtractedVariableUsage(String... attributes) {
        super();
        this.startOffset = Long.parseLong(attributes[0]);
        this.endOffset = Long.parseLong(attributes[1]);
        this.extractedCode = attributes[2];
        this.replacedBy = attributes[3];
    }

    @Override
    public String renderData() {
        return "<b>EXTRACTED VARIABLE USAGE<br>Extracted code:</b> " + extractedCode +
                "<br><b>Replaced by: </b>" + replacedBy + "<br>" + printAdditionalData();
    }

    @Override
    public String getType() {
        return "EXTRACTED_VARIABLE_USAGE";
    }
}
