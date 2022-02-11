package difflogic;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.tree.IElementType;
import javalanguage.psi.JavaTypes;
import models.Data;
import services.EditorService;

import java.util.List;
import java.util.Map;

public class DiffHighlighter {
    Map<Integer, List<Data>> diffMap;

    public DiffHighlighter() {
//        IDiffGenerator diffGenerator = new DiffGeneratorCmd();
//        boolean response = diffGenerator.generateDiff();
//
//        DiffGeneratedManagerFiles diffGeneratedManager = new DiffGeneratedManagerFiles();
//        Optional<Path> lfPath = diffGeneratedManager.getLatestGeneratedDiffPath();
//
//        DiffReaderFiles diffReaderFiles = new DiffReaderFiles();
//        ArrayList<DiffRow> diffs = diffReaderFiles.getDiffList(lfPath.get());
//
//        DiffMapper diffMapper = new DiffMapper(diffs);
//        diffMap = diffMapper.createDiffMap();
        Project project = ProjectManager.getInstance().getOpenProjects()[0];
        EditorService editorService = project.getService(EditorService.class);
//        ArrayList<DiffRow> diffs = editorService.getDiffsOfLastOpenedEditor();z
//        diffMap = editorService.getDiffMap();
        diffMap = editorService.getActiveEditorChanges();
//        DiffMapper diffMapper = new DiffMapper(diffs);
//        diffMap = diffMapper.createDiffMap();
    }

    public IElementType getLineHighlight(int line) {
        List<Data> actions = diffMap.get(line);
        Data action = ModificationDataUtils.getModificationDataFromLineActions(actions);
        if (action == null) {
            return JavaTypes.NOTMODIFIED;
        } else if (action.getType() == null) {
            return JavaTypes.NOTMODIFIED;
        } else {
            switch (action.getType()) {
                case "INS":
                    return JavaTypes.INSERTED;
                case "UPD":
                    return JavaTypes.UPDATED;
                case "UPD_MULTIPLE_TIMES":
                    return JavaTypes.UPDATEDMULTIPLETIMES;
                case "MOV":
                    return JavaTypes.MOVED;
                default:
                    return JavaTypes.NOTMODIFIED;
            }
        }
    }
}
