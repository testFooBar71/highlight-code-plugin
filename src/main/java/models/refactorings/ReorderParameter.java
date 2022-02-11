package models.refactorings;

import java.util.Arrays;
import java.util.List;

public class ReorderParameter extends RefactoringData {
    String[] oldParametersOrder;

    public ReorderParameter(String... attributes) {
        super();
        this.startOffset = Long.parseLong(attributes[0]);
        this.endOffset = Long.parseLong(attributes[1]);
        List<String> oldParametersOrder = Arrays.asList(attributes).subList(2, attributes.length);
        this.oldParametersOrder = oldParametersOrder.toArray(new String[0]);
    }

    @Override
    public String renderData() {
        return "<b>REORDERED PARAMETER<br>Old parameters order:</b><br>"
                + getOldParametersOrderString() + "<br>" + printAdditionalData();
    }

    private String getOldParametersOrderString() {
        StringBuilder result = new StringBuilder();
        for (String parameter: oldParametersOrder) {
            result.append(parameter).append(", ");
        }
        result.delete(result.length() - 2, result.length());
        return result.toString();
    }

    @Override
    public String getType() {
        return "REORDER_PARAMETER";
    }
}
