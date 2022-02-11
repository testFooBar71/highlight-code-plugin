package models;

import java.util.HashMap;
import java.util.Map;

public class ChangesSummary extends Data {

    Map<String, Integer> basicChangesAmount;
    Map<String, Integer> refactoringsAmount;

    public ChangesSummary() {
        basicChangesAmount = new HashMap<>();
        refactoringsAmount = new HashMap<>();
    }

    public void addChange(String change) {
        if (isBasicChange(change)) {
            int currentAmount = basicChangesAmount.get(change) != null ? basicChangesAmount.get(change) : 0;
            basicChangesAmount.put(change, currentAmount + 1);
        } else {
            int currentAmount = refactoringsAmount.get(change) != null ? refactoringsAmount.get(change) : 0;
            refactoringsAmount.put(change, currentAmount + 1);
        }
    }

    private boolean isBasicChange(String change) {
        return change.equals("INS") || change.equals("UPD") || change.equals("MOV") || change.equals("DEL");
    }

    private String parseChangeName(String changeName) {
        String nameWithSpaces = changeName.replaceAll("_", " ");
        String name = parseBasicAction(nameWithSpaces);
        return name.charAt(0) + name.substring(1).toLowerCase();
    }

    private String parseBasicAction(String name) {
        switch (name) {
            case "INS": return "INSERTS";
            case "UPD": return "UPDATES";
            case "MOV": return "MOVES";
            case "DEL": return "DELETIONS";
            default: return name;
        }
    }
    @Override
    public String renderData() {
        if (basicChangesAmount.size() == 0 && refactoringsAmount.size() == 0) {
            return "No changes have been applied to this file on the last commit";
        } else {
            StringBuilder content = new StringBuilder("<b>CHANGES SUMMARY</b><br>");
            if (basicChangesAmount.size() > 0) {
                content.append("<b>BASIC CHANGES:</b><br>");
                for (Map.Entry<String, Integer> changeAmount : basicChangesAmount.entrySet()) {
                    content.append("<b>").append(parseChangeName(changeAmount.getKey()))
                            .append(": </b>").append(changeAmount.getValue()).append("<br>");
                }
            }
            if (refactoringsAmount.size() > 0) {
                content.append("<b>REFACTORINGS:</b><br>");
                for (Map.Entry<String, Integer> changeAmount : refactoringsAmount.entrySet()) {
                    content.append("<b>").append(parseChangeName(changeAmount.getKey()))
                            .append(": </b>").append(changeAmount.getValue()).append("<br>");
                }
            }
            return content.toString();
        }
    }

    @Override
    public String getType() {
        return "CHANGES_SUMMARY";
    }
}
