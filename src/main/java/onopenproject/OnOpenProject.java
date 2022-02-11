package onopenproject;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import git.GitLocal;
import org.apache.batik.dom.svg12.Global;
import org.jetbrains.annotations.NotNull;
import services.EditorService;
import services.GitService;
import services.GlobalChangesService;

public class OnOpenProject implements ProjectManagerListener {

    @Override
    public void projectOpened(@NotNull Project project) {
        String projectPath = project.getBasePath();
        GitService gitService = project.getService(GitService.class);
        GitLocal gitLocal = new GitLocal(projectPath);
        project.getService(EditorService.class).initialize();
        gitLocal.openRepository();
        gitService.setRepository(gitLocal.getRepository());
        gitService.setLatestCommitHash(gitLocal.getLatestCommit().getName());
        project.getService(GlobalChangesService.class).addAllChanges(project, gitLocal.getLatestCommit());
    }

    @Override
    public void projectClosed(@NotNull Project project) {
    }
}
