package fileexplorer;

import com.intellij.ide.FileIconProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import difflogic.DiffMapGenerator;
import difflogic.DiffModifications;
import git.GitLocal;
import javalanguage.JavaIcons;
import models.Data;
import org.eclipse.jgit.revwalk.RevCommit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import services.GitService;
import services.GlobalChangesService;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public class ChangesIconProvider implements FileIconProvider {
    @Override
    public @Nullable Icon getIcon(@NotNull VirtualFile file, int flags, @Nullable Project project) {
        String absolutePath = file.getPath();
        String projectPath = project.getBasePath();
        String path = absolutePath.replaceAll(projectPath + "/", "");
        int numberOfChanges = project.getService(GlobalChangesService.class).getAmountOfChanges(path);
        if (numberOfChanges == 0) {
            return JavaIcons.NO_CHANGES;
        } else if (numberOfChanges < 10) {
            return JavaIcons.FEW_CHANGES;
        } else if (numberOfChanges < 25) {
            return JavaIcons.MEDIUM_CHANGES;
        } else {
            return JavaIcons.MANY_CHANGES;
        }
//        return JavaIcons.NO_CHANGES;

    }
}
