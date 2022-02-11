package models.refactorings;

public class PushDownMethod extends RefactoringData {
    String oldParentClass;
    String parentClassUrl;

    public PushDownMethod(String... attributes) {
        super();
        this.oldParentClass = attributes[0];
        this.parentClassUrl = attributes[1];
        this.startOffset = Long.parseLong(attributes[2]);
        this.endOffset = Long.parseLong(attributes[3]);
    }

    @Override
    public String renderData() {
        return "<b>PUSHED DOWN METHOD<br>This method was previously in:</b><br>"
                + oldParentClass + "<br>" + printAdditionalData() + "<br>Click on the label to go to " + oldParentClass;
    }

    @Override
    public String getType() {
        return "PUSH_DOWN_METHOD";
    }

    public String getParentClassUrl() {
        return parentClassUrl;
    }
}
