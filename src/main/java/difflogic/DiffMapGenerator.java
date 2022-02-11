package difflogic;

import actions.ActionsUtils;
import at.aau.softwaredynamics.classifier.entities.NodeInfo;
import at.aau.softwaredynamics.classifier.entities.SourceCodeChange;
import at.aau.softwaredynamics.gen.NodeType;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import compare.CompareUtils;
import editor.EditorUtils;
import git.GitLocal;
import models.ChangesSummary;
import models.Data;
import models.DataFactory;
import models.DiffRow;
import models.actions.ModificationData;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.refactoringminer.api.Refactoring;
import refactoringminer.RefactoringGenerator;
import refactoringminer.RefactoringMinerUtils;
import services.GitService;
import services.GlobalChangesService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class DiffMapGenerator {
    public Map<Integer, List<Data>> generateHighlightMapForEditor(Editor editor) {
        Project project = editor.getProject();
        GitService gitService = project.getService(GitService.class);
        Repository repository = gitService.getRepository();
        GitLocal gitLocal = new GitLocal(repository);
        String fileName = EditorUtils.getFileName(editor);
        String filePath = EditorUtils.getRelativePath(editor);
        DiffModifications diffModifications = new DiffModifications();
        RevCommit modificationCommit = diffModifications.getCommitWithLatestModification(fileName, gitLocal);
//        Map<Integer, Integer> amountOfTimes = diffModifications.buildNumberOfModifications(fileName, gitLocal);
        RefactoringGenerator refactoringGenerator = new RefactoringGenerator();
        List<Refactoring> refactorings = refactoringGenerator.getRefactorings(project, modificationCommit);
        Map<Integer, List<Data>> diffMap = getDiffMapOfCommit(modificationCommit, editor, gitLocal);
//        diffModifications.applyAmountOfTimesToDiffMap(diffMap, amountOfTimes);
        new RefactoringMinerUtils(project, modificationCommit).addRefactoringsToMap(refactorings, diffMap, filePath);
        return diffMap;
    }

    public Map<Integer, List<Data>> generateChangesMapForEditor(Editor editor, RevCommit sourceCommit, RevCommit destinationCommit) {
        Project project = editor.getProject();
        GitService gitService = project.getService(GitService.class);
        Repository repository = gitService.getRepository();
        GitLocal gitLocal = new GitLocal(repository);
        List<SourceCodeChange> sourceCodeChanges = getSourceCodeChangesOfCommits(sourceCommit, destinationCommit, editor, gitLocal);
        Map<Integer, List<Data>> changes = new HashMap<>();
//        if (!Arrays.stream(destinationCommit.getParents()).anyMatch(commit -> commit == sourceCommit)) {
//            Map<Integer, List<Data>> changesWithParent = generateChangesMapForEditor(editor, destinationCommit.getParents()[0], destinationCommit);
//            overrideChanges(changes, changesWithParent);
//        }
        RefactoringGenerator refactoringGenerator = new RefactoringGenerator();
        List<Refactoring> refactorings = refactoringGenerator.getRefactorings(project, destinationCommit);
        String filePath = EditorUtils.getRelativePath(editor);
        new RefactoringMinerUtils(project, destinationCommit, editor.getDocument()).addRefactoringsToMap(refactorings, changes, filePath);
        String previousFileContent = gitLocal.getFileContentOnCommit(editor, sourceCommit);
        addSourceCodeChangesToMap(sourceCodeChanges, changes, editor.getDocument(), destinationCommit, previousFileContent);
//        if (Arrays.stream(destinationCommit.getParents()).anyMatch(commit -> commit == sourceCommit)) {
//            RefactoringGenerator refactoringGenerator = new RefactoringGenerator();
//            List<Refactoring> refactorings = refactoringGenerator.getRefactorings(project, destinationCommit);
//            String filePath = EditorUtils.getRelativePath(editor);
//            new RefactoringMinerUtils(project).addRefactoringsToMap(refactorings, changes, filePath);
//        }
        ActionsUtils.addActionToLine(changes, 1, createChangesSummary(changes));
        return changes;
    }

    public Map<Integer, List<Data>> getChangesMapForEditor(Editor editor) {
        String path = EditorUtils.getRelativePath(editor);
        Map<Integer, List<Data>> changes = new HashMap<>();
        changes = editor.getProject().getService(GlobalChangesService.class).getChangesForFile(path);
        ActionsUtils.addActionToLine(changes, 1, createChangesSummary(changes));
        return changes;
    }

    public Data createChangesSummary(Map<Integer, List<Data>> changes) {
        ChangesSummary changesSummary = new ChangesSummary();
        for (Map.Entry<Integer, List<Data>> change : changes.entrySet()) {
            for (Data data : change.getValue()) {
                changesSummary.addChange(data.getType());
            }
        }
        return changesSummary;
    }

    public List<SourceCodeChange> getSourceCodeChangesOfCommits(RevCommit sourceCommit, RevCommit destinationCommit, Editor editor, GitLocal gitLocal) {
        String sourceFileContent = gitLocal.getFileContentOnCommit(editor, sourceCommit);
        String destinationFileContent = gitLocal.getFileContentOnCommit(editor, destinationCommit);
        return CompareUtils.getSourceCodeChanges(sourceFileContent, destinationFileContent);
    }

    public List<SourceCodeChange> getSourceCodeChangesOfCommits(RevCommit sourceCommit, RevCommit destinationCommit, VirtualFile file, GitLocal gitLocal, Project project) {
        String sourceFileContent = gitLocal.getFileContentOnCommit(file, sourceCommit, project);
        String destinationFileContent = gitLocal.getFileContentOnCommit(file, destinationCommit, project);
        return CompareUtils.getSourceCodeChanges(sourceFileContent, destinationFileContent);
    }

    public void addSourceCodeChangesToMap(List<SourceCodeChange> sourceCodeChanges, Map<Integer, List<Data>> changes, Document document, RevCommit commit, String previousFileContent) {
        for (SourceCodeChange sourceCodeChange : sourceCodeChanges) {
            NodeInfo nodeInfo = sourceCodeChange.getAction().getName().equals("DEL") ? sourceCodeChange.getSrcInfo() : sourceCodeChange.getDstInfo();
            if (String.valueOf(NodeType.getEnum(sourceCodeChange.getNodeType())).equals("METHOD_DECLARATION")
                    || String.valueOf(NodeType.getEnum(sourceCodeChange.getNodeType())).equals("TYPE_DECLARATION")
                    || String.valueOf(NodeType.getEnum(sourceCodeChange.getNodeType())).equals("IF_STATEMENT")) {
                int startLineNumber = nodeInfo.getStartLineNumber() < document.getLineCount() ? nodeInfo.getStartLineNumber() - 1 : document.getLineCount() - 1;
                long startOffset = document.getLineStartOffset(startLineNumber);
                long endOffset = document.getLineEndOffset(startLineNumber);
                if (!isInMap(startOffset, endOffset, changes)) {
                    PersonIdent author = commit.getAuthorIdent();
                    Date date = author.getWhen();
                    LocalDateTime commitDate = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
                    String previousLineContent = "";
                    if (sourceCodeChange.getAction().getName().equals("DEL")) {
                        List<String> previousFileLines = Arrays.asList(previousFileContent.split("\n"));
                        try {
                            previousLineContent = previousFileLines.get(nodeInfo.getStartLineNumber() - 1);
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("e");
                        }
                    } else if (sourceCodeChange.getAction().getName().equals("UPD")) {
                        List<String> previousFileLines = Arrays.asList(previousFileContent.split("\n"));
                        try {
                            previousLineContent = previousFileLines.get(sourceCodeChange.getSrcInfo().getStartLineNumber() - 1);
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("e");
                        }
                    } else if (sourceCodeChange.getAction().getName().equals("MOV")) {
                        previousLineContent = String.valueOf(sourceCodeChange.getSrcInfo().getStartLineNumber());
                    }
                    Data action = DataFactory.createModificationData(sourceCodeChange.getAction().getName(), author, commitDate, startOffset, endOffset, previousLineContent);
                    ActionsUtils.addActionToLineWithOffsets(changes, nodeInfo.getStartLineNumber(), action, startOffset, endOffset);
                }
            } else if (nodeInfo.getStartLineNumber() == nodeInfo.getEndLineNumber()) {
                long startLineOffset = document.getLineStartOffset(nodeInfo.getStartLineNumber() < document.getLineCount() ? nodeInfo.getStartLineNumber() - 1 : document.getLineCount() - 1);
                long startOffset = startLineOffset + nodeInfo.getStartOffset();
                long endOffset = startLineOffset + nodeInfo.getEndOffset();
                if (!isInMap(startOffset, endOffset, changes)) {
                    PersonIdent author = commit.getAuthorIdent();
                    Date date = author.getWhen();
                    LocalDateTime commitDate = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
                    String previousLineContent = "";
                    if (sourceCodeChange.getAction().getName().equals("DEL")) {
                        List<String> previousFileLines = Arrays.asList(previousFileContent.split("\n"));
                        try {
                            previousLineContent = previousFileLines.get(nodeInfo.getStartLineNumber() - 1);
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("e");
                        }
                    } else if (sourceCodeChange.getAction().getName().equals("UPD")) {
                        List<String> previousFileLines = Arrays.asList(previousFileContent.split("\n"));
                        try {
                            previousLineContent = previousFileLines.get(sourceCodeChange.getSrcInfo().getStartLineNumber() - 1);
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("e");
                        }
                    } else if (sourceCodeChange.getAction().getName().equals("MOV")) {
                        previousLineContent = String.valueOf(sourceCodeChange.getSrcInfo().getStartLineNumber());
                    }
                    Data action = DataFactory.createModificationData(sourceCodeChange.getAction().getName(), author, commitDate, startOffset, endOffset, previousLineContent);
                    ActionsUtils.addActionToLineWithOffsets(changes, nodeInfo.getStartLineNumber(), action, startOffset, endOffset);
                }
            }
        }
    }

    private boolean isInMap(long startOffset, long endOffset, Map<Integer, List<Data>> changes) {
        for (Map.Entry<Integer, List<Data>> changesEntry : changes.entrySet()) {
            if (changesEntry.getValue().stream().anyMatch(data -> containsInterval(startOffset, endOffset, data.getStartOffset(), data.getEndOffset()))) {
                return true;
            }
        }
        return false;
    }

    private boolean containsInterval(long startOffset, long endOffset, long dataStartOffset, long dataEndOffset) {
        boolean contains = startOffset <= dataStartOffset && endOffset >= dataEndOffset;
        boolean isInside = startOffset >= dataStartOffset && endOffset <= dataEndOffset;
        boolean isPartiallyOnLeft = startOffset <= dataStartOffset && endOffset <= dataEndOffset && endOffset > dataStartOffset;
        boolean isPartiallyOnRight = startOffset >= dataStartOffset && endOffset >= dataEndOffset && startOffset < dataEndOffset;
        return contains || isInside || isPartiallyOnLeft || isPartiallyOnRight;
    }

    public Map<Integer, List<Data>> generateChangesMapForFile(VirtualFile file, RevCommit sourceCommit, RevCommit destinationCommit, Project project) {
        GitService gitService = project.getService(GitService.class);
        Repository repository = gitService.getRepository();
        GitLocal gitLocal = new GitLocal(repository);
        List<SourceCodeChange> sourceCodeChanges = getSourceCodeChangesOfCommits(sourceCommit, destinationCommit, file, gitLocal, project);
        Map<Integer, List<Data>> changes = new HashMap<>();
        RefactoringGenerator refactoringGenerator = new RefactoringGenerator();
        List<Refactoring> refactorings = refactoringGenerator.getRefactorings(project, destinationCommit);
        String filePath = getFilePath(file, project);
        Document document = FileDocumentManager.getInstance().getDocument(file);
        new RefactoringMinerUtils(project, destinationCommit, document).addRefactoringsToMap(refactorings, changes, filePath);
        String previousFileContent = gitLocal.getFileContentOnCommit(file, sourceCommit, project);
        addSourceCodeChangesToMap(sourceCodeChanges, changes, document, destinationCommit, previousFileContent);
        return changes;
    }

    private Map<Integer, List<Data>> getDiffMapOfCommit(RevCommit commit, Editor editor, GitLocal gitLocal) {
        DiffEntry diffEntry = gitLocal.getDiffEntryOfCommit(commit, editor);
        if (diffEntry == null) {
            return new HashMap<Integer, List<Data>>();
        }
        String commitFileContent = gitLocal.getCurrentCommitFileContent(diffEntry);
        String previousFileContent = gitLocal.getPreviousCommitFileContent(diffEntry);
        List<DiffRow> diffRows = CompareUtils.getDiffChanges(previousFileContent, commitFileContent);
        return new DiffMapper(diffRows, commit, previousFileContent).createDiffMap();
    }

    void overrideChanges(Map<Integer, List<Data>> changes, Map<Integer, List<Data>> changesWithParent) {
        for (Map.Entry<Integer, List<Data>> entry : changes.entrySet()) {
            List<Data> dataList = entry.getValue();
            for (int i = 0; i < dataList.size(); i++) {
                if (!contains(dataList.get(i), changesWithParent, entry.getKey())) {
                    ModificationData modificationData = (ModificationData) dataList.get(i);
                    modificationData.setOnParent(false);
                    dataList.set(i, modificationData);
                }
            }
            changes.put(entry.getKey(), dataList);
        }
    }

    private String getFilePath(VirtualFile file, Project project) {
        String filePath = file.getPath();
        String projectPath = project.getBasePath();
        return filePath.replaceAll(projectPath + "/", "");
    }

    boolean contains(Data dataToSearch, Map<Integer, List<Data>> changesWithParent, int line) {
        for (Map.Entry<Integer, List<Data>> entry : changesWithParent.entrySet()) {
            if (line == entry.getKey() && entry.getValue().stream().anyMatch(data -> data.getType().equals(dataToSearch.getType()))) {
                return true;
            }
        }
        return false;
    }
}
