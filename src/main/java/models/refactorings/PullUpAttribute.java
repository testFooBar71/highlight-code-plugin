package models.refactorings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PullUpAttribute extends RefactoringData {
    private final String parentClass;
    private String[] oldClasses;

    public PullUpAttribute(String... attributes) {
        super();
        this.parentClass = attributes[0];
        this.startOffset = Long.parseLong(attributes[1]);
        this.endOffset = Long.parseLong(attributes[2]);
        List<String> oldClassesList = Arrays.asList(attributes).subList(3, attributes.length);
        this.oldClasses = oldClassesList.toArray(new String[0]);
    }

    @Override
    public String renderData() {
        return "<b>PULLED UP ATTRIBUTE<br>Classes where this attribute was:</b><br>"
                + getClassesWhereAttributeWas() + printAdditionalData();
    }

    private String getClassesWhereAttributeWas() {
        StringBuilder result = new StringBuilder();
        for (String parameter: oldClasses) {
            result.append(parameter).append("<br>");
        }
        return result.toString();
    }

    public void addOldClass(String oldClass) {
        List<String> oldClassesList = new ArrayList<>(Arrays.asList(oldClasses));
        oldClassesList.add(oldClass);
        oldClasses = oldClassesList.toArray(new String[0]);
    }

    public String[] getOldClasses() {
        return oldClasses;
    }

    public String getParentClass() {
        return parentClass;
    }

    @Override
    public String getType() {
        return "PULL_UP_ATTRIBUTE";
    }
}
