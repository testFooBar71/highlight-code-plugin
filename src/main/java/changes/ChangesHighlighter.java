package changes;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.tree.IElementType;
import difflogic.ModificationDataUtils;
import javalanguage.psi.JavaTypes;
import models.Data;
import org.apache.tools.ant.taskdefs.Java;
import services.EditorService;

import java.util.List;
import java.util.Map;

public class ChangesHighlighter {
    Map<Integer, List<Data>> changesMap;

    public ChangesHighlighter() {
        Project project = ProjectManager.getInstance().getOpenProjects()[0];
        EditorService editorService = project.getService(EditorService.class);
        changesMap = editorService.getActiveEditorChanges();
    }

    public IElementType getCharHighlight(int line, long currentOffset) {
        List<Data> actions = changesMap.get(line);
        if (actions != null) {
            for (Data action: actions) {
                if (currentOffset >= action.getStartOffset() && currentOffset < action.getEndOffset()) {
                    if (isInsertion(action)) {
                        return JavaTypes.INSERTED;
                    } else if (isUpdate(action)) {
                        return JavaTypes.UPDATED;
                    } else if (isMove(action)) {
                        return JavaTypes.MOVED;
                    } else {
                        return JavaTypes.NOTMODIFIED;
                    }
                }
            }
        }
        return JavaTypes.NOTMODIFIED;
    }

    private boolean isInsertion(Data action) {
        return action.getType().equals("INS") || action.getType().equals("ADD_PARAMETER") ||
                action.getType().equals("EXTRACTED_METHOD") ||
                action.getType().equals("EXTRACTED_METHOD_CALL") ||
                action.getType().equals("EXTRACTED_VARIABLE") ||
                action.getType().equals("EXTRACT_INTERFACE") ||
                action.getType().equals("EXTRACT_SUPERCLASS");
    }

    private boolean isUpdate(Data action) {
        return action.getType().equals("UPD") || action.getType().equals("RENAME_METHOD") ||
                action.getType().equals("RENAME_CLASS") || action.getType().equals("RENAME_VARIABLE") ||
                action.getType().equals("RENAME_PARAMETER") || action.getType().equals("RENAME_ATTRIBUTE") ||
                action.getType().equals("RENAME_ATTRIBUTE") ||
                action.getType().equals("CHANGE_VARIABLE_TYPE") ||
                action.getType().equals("EXTRACTED_VARIABLE_USAGE") ||
                action.getType().equals("CHANGE_PARAMETER_TYPE") ||
                action.getType().equals("CHANGE_RETURN_TYPE") ||
                action.getType().equals("CHANGE_ATTRIBUTE_TYPE");
    }

    private boolean isMove(Data action) {
        return action.getType().equals("MOV") || action.getType().equals("PULL_UP_METHOD") ||
                action.getType().equals("PULL_UP_ATTRIBUTE") ||
                action.getType().equals("PUSH_DOWN_METHOD") ||
                action.getType().equals("PUSH_DOWN_ATTRIBUTE") ||
                action.getType().equals("PUSH_DOWN_ATTRIBUTE") ||
                action.getType().equals("REORDER_PARAMETER");
    }
}
