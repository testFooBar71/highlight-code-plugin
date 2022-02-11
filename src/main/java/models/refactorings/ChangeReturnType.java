package models.refactorings;

public class ChangeReturnType extends RefactoringData {
    String oldType;

    public ChangeReturnType(String... attributes) {
        super();
        this.oldType = attributes[0];
        this.startOffset = Long.parseLong(attributes[1]);
        this.endOffset = Long.parseLong(attributes[2]);
    }

    @Override
    public String renderData() {
        return "<b>CHANGED RETURN TYPE<br>Old return type:</b> " + oldType
                + "<br>" + printAdditionalData();
    }

    @Override
    public String getType() {
        return "CHANGE_RETURN_TYPE";
    }
}
