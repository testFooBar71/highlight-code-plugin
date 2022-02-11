package actions;

import models.Data;
import models.actions.ModificationData;
import models.refactorings.PullUpAttribute;
import models.refactorings.PullUpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class ActionsUtils {
    public static void addActionToLine(Map<Integer, List<Data>> actionsMap, int line, Data action) {
        List<Data> lineActions = actionsMap.get(line);
        if (lineActions == null) {
            lineActions = new ArrayList<>();
        }
        if (lineActions
                .stream()
                .noneMatch(lineAction -> lineAction instanceof ModificationData && lineAction.getType().equals(action.getType()))) {
            lineActions.add(action);
        } else {
            Data data = lineActions.stream().filter(lineAction -> lineAction instanceof ModificationData && lineAction.getType().equals(action.getType())).findFirst().get();
            data.setStartOffset(Math.min(action.getStartOffset(), data.getStartOffset()));
            data.setEndOffset(Math.max(action.getEndOffset(), data.getEndOffset()));
            lineActions.set(findIndex(lineActions, action), data);
        }
        actionsMap.put(line, lineActions);
    }

    public static void addActionToLineWithOffsets(Map<Integer, List<Data>> actionsMap, int line, Data action, long startOffset, long endOffset) {
        List<Data> lineActions = actionsMap.get(line);
        if (lineActions == null) {
            lineActions = new ArrayList<>();
        }
        if (lineActions
                .stream()
                .noneMatch(lineAction -> lineAction instanceof ModificationData && lineAction.getType().equals(action.getType()))) {
            lineActions.add(action);
        } else {
            Data data = lineActions.stream().filter(lineAction -> lineAction instanceof ModificationData && lineAction.getType().equals(action.getType())).findFirst().get();
            data.setStartOffset(Math.min(startOffset, data.getStartOffset()));
            data.setEndOffset(Math.max(endOffset, data.getEndOffset()));
            lineActions.set(findIndex(lineActions, action), data);
        }
        actionsMap.put(line, lineActions);
    }

    private static int findIndex(List<Data> lineActions, Data action) {
        return IntStream.range(0, lineActions.size())
                .filter(i -> lineActions.get(i) instanceof ModificationData && lineActions.get(i).getType().equals(action.getType()))
                .findFirst()
     .orElse(-1);
    }

    public static void addPullUpAttribute(Map<Integer, List<Data>> actionsMap, int line, PullUpAttribute action) {
        List<Data> lineActions = actionsMap.get(line);
        if (lineActions == null) {
            lineActions = new ArrayList<>();
        }
        if (lineActions.stream().noneMatch(lineAction -> lineAction instanceof PullUpAttribute && ((PullUpAttribute) lineAction).getParentClass().equals(action.getParentClass()))) {
            lineActions.add(action);
        } else {
            boolean hasInserted = false;
            for (int i = 0; i < lineActions.size(); i++) {
                if (lineActions.get(i) instanceof PullUpAttribute) {
                    PullUpAttribute pullUpAttribute = (PullUpAttribute) lineActions.get(i);
                    if (pullUpAttribute.getParentClass().equals(action.getParentClass())) {
                        pullUpAttribute.addOldClass(action.getOldClasses()[0]);
                        lineActions.set(i, pullUpAttribute);
                        hasInserted = true;
                        break;
                    }
                }
            }
            if (!hasInserted) {
                lineActions.add(action);
            }
        }
        actionsMap.put(line, lineActions);
    }

    public static void addPullUpMethod(Map<Integer, List<Data>> actionsMap, int line, PullUpMethod action) {
        List<Data> lineActions = actionsMap.get(line);
        if (lineActions == null) {
            lineActions = new ArrayList<>();
        }
        if (lineActions.stream().noneMatch(lineAction -> lineAction instanceof PullUpMethod && ((PullUpMethod) lineAction).getParentClass().equals(action.getParentClass()))) {
            lineActions.add(action);
        } else {
            boolean hasInserted = false;
            for (int i = 0; i < lineActions.size(); i++) {
                if (lineActions.get(i) instanceof PullUpMethod) {
                    PullUpMethod pullUpMethod = (PullUpMethod) lineActions.get(i);
                    if (pullUpMethod.getParentClass().equals(action.getParentClass())) {
                        pullUpMethod.addOldClass(action.getOldClasses()[0]);
                        lineActions.set(i, pullUpMethod);
                        hasInserted = true;
                        break;
                    }
                }
            }
            if (!hasInserted) {
                lineActions.add(action);
            }
        }
        actionsMap.put(line, lineActions);
    }
}
