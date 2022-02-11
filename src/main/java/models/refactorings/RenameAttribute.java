package models.refactorings;

public class RenameAttribute extends RefactoringData {
    String oldName;

    public RenameAttribute(String... attributes) {
        super();
        this.oldName = attributes[0];
        this.startOffset = Long.parseLong(attributes[1]);
        this.endOffset = Long.parseLong(attributes[2]);
    }

    @Override
    public String renderData() {
        return "<b>RENAMED ATTRIBUTE<br>Old attribute name:</b> " + oldName + "<br>" + printAdditionalData();
    }

    @Override
    public String getType() {
        return "RENAME_ATTRIBUTE";
    }
}
