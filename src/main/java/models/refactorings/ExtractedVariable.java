package models.refactorings;

public class ExtractedVariable extends RefactoringData {

    public ExtractedVariable(String... attributes) {
        super();
        this.startOffset = Long.parseLong(attributes[0]);
        this.endOffset = Long.parseLong(attributes[1]);
    }

    @Override
    public String renderData() {
        return "<b>EXTRACTED VARIABLE</b><br>" + printAdditionalData();
    }

    @Override
    public String getType() {
        return "EXTRACTED_VARIABLE";
    }
}
