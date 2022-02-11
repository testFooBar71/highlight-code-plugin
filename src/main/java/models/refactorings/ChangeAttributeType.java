package models.refactorings;

public class ChangeAttributeType extends RefactoringData {
    String oldType;

    public ChangeAttributeType(String... attributes) {
        super();
        this.oldType = attributes[0];
        this.startOffset = Long.parseLong(attributes[1]);
        this.endOffset = Long.parseLong(attributes[2]);
    }

    @Override
    public String renderData() {
        return "<b>CHANGED ATTRIBUTE TYPE<br>Old attribute type:</b> "+ oldType
                + "<br>" + printAdditionalData();
    }

    @Override
    public String getType() {
        return "CHANGE_ATTRIBUTE_TYPE";
    }

}
