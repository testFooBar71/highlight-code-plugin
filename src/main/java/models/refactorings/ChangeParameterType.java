package models.refactorings;

public class ChangeParameterType extends RefactoringData {
    String parameter;
    String oldType;

    public ChangeParameterType(String... attributes) {
        super();
        this.parameter = attributes[0];
        this.oldType = attributes[1];
        this.startOffset = Long.parseLong(attributes[2]);
        this.endOffset = Long.parseLong(attributes[3]);
    }

    @Override
    public String renderData() {
        return "<b>CHANGED PARAMETER TYPE<br>Parameter:</b> " + parameter + "<br>"
                + "<b>Old parameter type:</b> " + oldType + "<br>" + printAdditionalData() + "<br>Click on the label to select the parameter with type changed";
    }

    @Override
    public String getType() {
        return "CHANGE_PARAMETER_TYPE";
    }
}
