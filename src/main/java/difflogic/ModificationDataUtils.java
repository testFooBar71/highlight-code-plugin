package difflogic;

import models.Data;
import models.actions.ModificationData;

import java.util.List;
import java.util.stream.Collectors;

public class ModificationDataUtils {
    public static Data getModificationDataFromLineActions(List<Data> actions) {
        try {
//            Predicate<Data> byType = action -> action.getType().equals("UPD") || action.getType().equals("INS") || action.getType().equals("MOV") || action.getType().equals("DEL") || action.getType().equals("UPD_MULTIPLE_TIMES");
            if (actions != null) {
                return actions.stream()
                        .filter(action -> action instanceof ModificationData)
                        .collect(Collectors.toList())
                        .get(0);
            } else {
                return null;
            }
        } catch(IndexOutOfBoundsException exception) {
            return null;
        }
    }
}
