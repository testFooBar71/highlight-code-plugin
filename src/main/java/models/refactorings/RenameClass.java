package models.refactorings;

public class RenameClass extends RefactoringData {
    String oldName;

    public RenameClass(String... attributes) {
        super();
        this.oldName = attributes[0];
        this.startOffset = Long.parseLong(attributes[1]);
        this.endOffset = Long.parseLong(attributes[2]);
    }

    @Override
    public String renderData() {
        return "<b>RENAMED CLASS<br>Old class name:</b> " + oldName + "<br>" + printAdditionalData();
    }

    @Override
    public String getType() {
        return "RENAME_CLASS";
    }
}
