package models.refactorings;

public class ChangeVariableType extends RefactoringData {
    String oldType;

    public ChangeVariableType(String... attributes) {
        super();
        this.oldType = attributes[0];
        this.startOffset = Long.parseLong(attributes[1]);
        this.endOffset = Long.parseLong(attributes[2]);
    }

    @Override
    public String renderData() {
        return "<b>CHANGED VARIABLE TYPE<br>Old variable type:</b> " + oldType
                + "<br>" + printAdditionalData();
    }

    @Override
    public String getType() {
        return "CHANGE_VARIABLE_TYPE";
    }

}
