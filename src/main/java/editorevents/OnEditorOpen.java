package editorevents;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.project.Project;
import difflogic.DiffMapGenerator;
import difflogic.DiffModifications;
import editor.EditorUtils;
import git.GitLocal;
import models.Data;
import models.EditorData;
import org.eclipse.jgit.revwalk.RevCommit;
import org.jetbrains.annotations.NotNull;
import services.EditorService;
import services.GitService;
import visualelements.VisualElementsUtils;

import java.util.*;

public class OnEditorOpen implements EditorFactoryListener {

    @Override
    public void editorCreated(@NotNull EditorFactoryEvent event) {
        Editor editor = event.getEditor();
        EditorService editorService = editor.getProject().getService(EditorService.class);
        new VisualElementsUtils().registerEditorToCoverLayerManager(editor);
        if (editorService.editorIsOnMap(editor)) {
            EditorData editorData = editorService.getEditorData(editor);
//            Map<Integer, List<Data>> changes = new DiffMapGenerator().generateChangesMapForEditor(editor, editorData.getSourceCommit(), editorData.getDestinationCommit());
            Map<Integer, List<Data>> changes = new DiffMapGenerator().getChangesMapForEditor(editor);
            editorData.setActive(true);
            editorData.setChanges(changes);
            editorService.setEditorWithData(editor, editorData);
        } else {
            RevCommit latestCommit = getLatestCommit(editor.getProject());
            RevCommit previousCommit = latestCommit.getParents()[0];
//            Map<Integer, List<Data>> changes = new DiffMapGenerator().generateChangesMapForEditor(editor, previousCommit, latestCommit);
            Map<Integer, List<Data>> changes = new DiffMapGenerator().getChangesMapForEditor(editor);
            EditorData editorData = new EditorData(changes, true, previousCommit, latestCommit);
            editorService.setEditorWithData(editor, editorData);
        }
    }

    private RevCommit getLatestCommit(Project project) {
        GitService gitService = project.getService(GitService.class);
        GitLocal gitLocal = new GitLocal(gitService.getRepository());
        return gitLocal.getLatestCommit();
    }

    @Override
    public void editorReleased(@NotNull EditorFactoryEvent event) { }
}
