package models.refactorings;

public class RenameParameter extends RefactoringData {
    String parameterType;
    String oldName;

    public RenameParameter(String... attributes) {
        super();
        this.parameterType = attributes[0];
        this.oldName = attributes[1];
        this.startOffset = Long.parseLong(attributes[2]);
        this.endOffset = Long.parseLong(attributes[3]);
    }

    @Override
    public String renderData() {
        return "<b>RENAMED PARAMETER<br>Old parameter name:</b> "
                + parameterType + " " + oldName + "<br>" + printAdditionalData() + "<br>Click on the label to select the renamed parameter";
    }

    @Override
    public String getType() {
        return "RENAME_PARAMETER";
    }

}
