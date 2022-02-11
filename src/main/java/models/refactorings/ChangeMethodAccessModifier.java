package models.refactorings;

public class ChangeMethodAccessModifier extends RefactoringData {
    String previousMethodAccessModifier;

    public ChangeMethodAccessModifier(String... attributes) {
        super();
        this.previousMethodAccessModifier = attributes[0];
        this.startOffset = Long.parseLong(attributes[1]);
        this.endOffset = Long.parseLong(attributes[2]);
    }

    @Override
    public String renderData() {
        return "<b>CHANGE METHOD ACCESS MODIFIER<br>Previous method access modifier:</b> "
                + previousMethodAccessModifier + "<br>" + printAdditionalData();
    }

    @Override
    public String getType() {
            return "CHANGE_METHOD_ACCESS_MODIFIER";
    }

}
