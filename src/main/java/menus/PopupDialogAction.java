package menus;

import at.aau.softwaredynamics.classifier.entities.SourceCodeChange;
import at.aau.softwaredynamics.runner.util.GitHelper;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.pom.Navigatable;
import compare.CompareUtils;
import difflogic.DiffMapGenerator;
import git.GitLocal;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.jetbrains.annotations.NotNull;
import services.GitService;

import java.io.IOException;
import java.util.List;

public class PopupDialogAction extends AnAction {

    @Override
    public void update(AnActionEvent e) {
        // Using the event, evaluate the context, and enable or disable the action.
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        Project currentProject = event.getProject();
        GitService gitService = currentProject.getService(GitService.class);
        Repository repository = gitService.getRepository();
        GitLocal gitLocal = new GitLocal(repository);
        RevCommit latestCommit = gitLocal.getLatestCommit();
        RevCommit parent = latestCommit.getParents()[0];
        List<DiffEntry> diffs = GitHelper.getDiffs(repository, latestCommit, parent);
        for (DiffEntry diff : diffs) {
            VirtualFile file = VirtualFileManager.getInstance().findFileByUrl("file://" + currentProject.getBasePath() + "/" + diff.getNewPath());
            List<SourceCodeChange> changes = new DiffMapGenerator().getSourceCodeChangesOfCommits(parent, latestCommit, file, gitLocal, currentProject);
            System.out.println("test");
        }
        StringBuilder dlgMsg = new StringBuilder(event.getPresentation().getText() + " Selected!");
        String dlgTitle = event.getPresentation().getDescription();
        Navigatable nav = event.getData(CommonDataKeys.NAVIGATABLE);
        if (nav != null) {
            dlgMsg.append(String.format("\nSelected Element: %s", nav.toString()));
        }
        Messages.showMessageDialog(currentProject, dlgMsg.toString(), dlgTitle, Messages.getInformationIcon());
    }

}
