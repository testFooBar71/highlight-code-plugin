package refactoringminer;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import git.GitLocal;
import models.refactoringminer.RefactoringMinerOutput;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.refactoringminer.api.GitHistoryRefactoringMiner;
import org.refactoringminer.api.Refactoring;
import org.refactoringminer.api.RefactoringHandler;
import org.refactoringminer.rm1.GitHistoryRefactoringMinerImpl;
import org.refactoringminer.util.GitServiceImpl;
import services.GitService;

import java.util.ArrayList;
import java.util.List;

public class RefactoringGenerator {
    public RefactoringMinerOutput generateRefactorings(Project project) {
        String projectPath = project.getBasePath();
        GitService gitService = project.getService(GitService.class);
        GitLocal gitLocal = new GitLocal(gitService.getRepository());
        RevCommit latestCommit = gitLocal.getLatestCommit();
        RefactoringMinerCmd refactoringMinerCmd = new RefactoringMinerCmd();
        return refactoringMinerCmd.runRefactoringMiner(projectPath, latestCommit.getName());
    }

    public List<Refactoring> getRefactorings(Project project, RevCommit commit) {
        GitService gitService = project.getService(GitService.class);
        Repository repository = gitService.getRepository();
        String commitSha = commit.getName();
        GitHistoryRefactoringMiner miner = new GitHistoryRefactoringMinerImpl();
        List<Refactoring> myRefactorings = new ArrayList<>();
        miner.detectAtCommit(repository, commitSha, new RefactoringHandler() {
            @Override
            public void handle(String commitId, List<Refactoring> refactorings) {
                myRefactorings.addAll(refactorings);
            }
        });
        return myRefactorings;
    }
}
