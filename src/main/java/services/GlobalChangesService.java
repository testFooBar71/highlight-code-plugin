package services;

import actions.ActionsUtils;
import at.aau.softwaredynamics.classifier.entities.SourceCodeChange;
import at.aau.softwaredynamics.runner.util.GitHelper;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import difflogic.DiffMapGenerator;
import editor.EditorUtils;
import git.GitLocal;
import models.Data;
import models.EditorData;
import models.JavaFileData;
import models.refactorings.PullUpAttribute;
import models.refactorings.PullUpMethod;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.refactoringminer.api.Refactoring;
import refactoringminer.RefactoringGenerator;
import refactoringminer.RefactoringMinerUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalChangesService {
    private Map<String, JavaFileData> files;

    public Map<Integer, List<Data>> getChangesForFile(String path) {
        if (files.get(path) == null) {
            return new HashMap<>();
        } else {
            return files.get(path).getChanges();
        }
    }

    public int getAmountOfChanges(String filePath) {
        JavaFileData fileData = files.get(filePath);
        if (fileData != null) {
            return fileData.countChanges();
        } else {
            return 0;
        }
    }

    public void addAllChanges(Project project, RevCommit commit) {
        files = new HashMap<>();
        RefactoringGenerator refactoringGenerator = new RefactoringGenerator();
        List<Refactoring> refactorings = refactoringGenerator.getRefactorings(project, commit);
        new RefactoringMinerUtils(project, commit).addAllRefactorings(refactorings);

        Repository repository = project.getService(GitService.class).getRepository();
        GitLocal gitLocal = new GitLocal(repository);
        RevCommit parentCommit = commit.getParents()[0];
        List<DiffEntry> diffs = GitHelper.getDiffs(repository, commit, parentCommit);
        for (DiffEntry diff : diffs) {
            if (!diff.getChangeType().equals(DiffEntry.ChangeType.DELETE)) {
                VirtualFile file = VirtualFileManager.getInstance().findFileByUrl("file://" + project.getBasePath() + "/" + diff.getNewPath());
                List<SourceCodeChange> changes = new DiffMapGenerator().getSourceCodeChangesOfCommits(parentCommit, commit, file, gitLocal, project);
                Document document = FileDocumentManager.getInstance().getDocument(file);
                String previousFileContent = gitLocal.getFileContentOnCommit(file, parentCommit, project);
                addSourceCodeChanges(diff.getNewPath(), changes, document, commit, previousFileContent);
            }
        }
    }

    public void addSourceCodeChanges(String filePath, List<SourceCodeChange> sourceCodeChanges, Document document, RevCommit commit, String previousFileContent) {
        if (files.get(filePath) == null) {
            addNewFileWithSourceCodeChanges(filePath, sourceCodeChanges, document, commit, previousFileContent);
        } else {
            addSourceCodeChangesToFile(filePath, sourceCodeChanges, document, commit, previousFileContent);
        }
    }

    public void addNewFileWithSourceCodeChanges(String filePath, List<SourceCodeChange> sourceCodeChanges, Document document, RevCommit commit, String previousFileContent) {
        Map<Integer, List<Data>> changes = new HashMap<>();
        new DiffMapGenerator().addSourceCodeChangesToMap(sourceCodeChanges, changes, document, commit, previousFileContent);
        JavaFileData fileData = new JavaFileData(changes);
        files.put(filePath, fileData);
    }

    public void addSourceCodeChangesToFile(String filePath, List<SourceCodeChange> sourceCodeChanges, Document document, RevCommit commit, String previousFileContent) {
        JavaFileData fileData = files.get(filePath);
        Map<Integer, List<Data>> changes = fileData.getChanges();
        new DiffMapGenerator().addSourceCodeChangesToMap(sourceCodeChanges, changes, document, commit, previousFileContent);
        fileData.setChanges(changes);
        files.put(filePath, fileData);
    }

    public void addChange(String filePath, int line, Data action) {
        if (files.get(filePath) == null) {
            addNewFileWithChange(filePath, line, action);
        } else {
            addChangeToFile(filePath, line, action);
        }
    }

    private void addNewFileWithChange(String filePath, int line, Data action) {
        Map<Integer, List<Data>> changes = new HashMap<>();
        if (action instanceof PullUpAttribute) {
            ActionsUtils.addPullUpAttribute(changes, line, (PullUpAttribute) action);
        } else if (action instanceof PullUpMethod) {
            ActionsUtils.addPullUpMethod(changes, line, (PullUpMethod) action);
        } else {
            ActionsUtils.addActionToLine(changes, line, action);
        }
        JavaFileData fileData = new JavaFileData(changes);
        files.put(filePath, fileData);
    }

    private void addChangeToFile(String filePath, int line, Data action) {
        JavaFileData fileData = files.get(filePath);
        Map<Integer, List<Data>> changes = fileData.getChanges();
        if (action instanceof PullUpAttribute) {
            ActionsUtils.addPullUpAttribute(changes, line, (PullUpAttribute) action);
        } else if (action instanceof PullUpMethod) {
            ActionsUtils.addPullUpMethod(changes, line, (PullUpMethod) action);
        } else {
            ActionsUtils.addActionToLine(changes, line, action);
        }
        fileData.setChanges(changes);
        files.put(filePath, fileData);
    }
}
