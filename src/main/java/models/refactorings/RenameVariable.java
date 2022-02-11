package models.refactorings;

public class RenameVariable extends RefactoringData {
    String oldName;

    public RenameVariable(String... attributes) {
        super();
        this.oldName = attributes[0];
        this.startOffset = Long.parseLong(attributes[1]);
        this.endOffset = Long.parseLong(attributes[2]);
    }

    @Override
    public String renderData() {
        return "<b>RENAMED VARIABLE<br>Old variable name:</b> " + oldName + "<br>" + printAdditionalData();
    }

    @Override
    public String getType() {
        return "RENAME_VARIABLE";
    }

}
