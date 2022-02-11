package difflogic;

import com.intellij.openapi.editor.Editor;
import compare.CompareUtils;
import editor.EditorUtils;
import git.GitLocal;
import models.*;
import models.actions.Inserted;
import models.actions.Updated;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.revwalk.RevCommit;
import services.GitService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiffModifications {
    public Map<Integer, Integer> buildNumberOfModifications(String fileName, GitLocal gitLocal) {
        Map<Integer, Integer> amountOfTimes = new HashMap<>();
        Map<Integer, List<Data>> diffMap;
        List<RevCommit> commits = gitLocal.getSelectedLatestCommitsAscendant(5);
        for (RevCommit commit : commits) {
            List<DiffRow> diffRows = generateDiffWithPreviousCommit(commit, fileName, gitLocal);
            diffMap = new DiffMapper(diffRows, commit).createDiffMap();
            for (Map.Entry<Integer, List<Data>> diffsEntry : diffMap.entrySet()) {
                handleDiffEntry(amountOfTimes, diffsEntry);
            }
        }
        return amountOfTimes;
    }

    public RevCommit getCommitWithLatestModification(String fileName, GitLocal gitLocal) {
        RevCommit commitWithLatestModification = null;
        List<RevCommit> commits = gitLocal.getSelectedLatestCommitsDescendant(10);
        for (RevCommit commit : commits) {
            List<DiffRow> diffRows = generateDiffWithPreviousCommit(commit, fileName, gitLocal);
            if (!diffRows.isEmpty()) {
                commitWithLatestModification = commit;
                break;
            }
        }
        if (commitWithLatestModification == null) {
            commitWithLatestModification = commits.get(1);
        }
        return commitWithLatestModification;
    }

    public RevCommit getCommitWithLatestModification(Editor editor) {
        GitService gitService = editor.getProject().getService(GitService.class);
        String fileName = EditorUtils.getFileName(editor);
        GitLocal gitLocal = new GitLocal(gitService.getRepository());
        RevCommit commitWithLatestModification = null;
        List<RevCommit> commits = gitLocal.getSelectedLatestCommitsDescendant(25);
        for (RevCommit commit : commits) {
            List<DiffRow> diffRows = generateDiffWithPreviousCommit(commit, fileName, gitLocal);
            if (!diffRows.isEmpty()) {
                commitWithLatestModification = commit;
                break;
            }
        }
        if (commitWithLatestModification == null) {
            commitWithLatestModification = commits.get(0);
        }
        return commitWithLatestModification;
    }

//    public void applyAmountOfTimesToDiffMap(Map<Integer, List<Data>> diffMap, Map<Integer, Integer> amountOfTimes) {
//        for (Map.Entry<Integer, Integer> line: amountOfTimes.entrySet()) {
//            if (line.getValue() >= 5) {
//                List<Data> modifications = diffMap.get(line.getKey());
//                Data modification = modifications.stream().filter(mod -> mod.getType().equals("UPD")).findFirst().get();
//                int index = modifications.indexOf(modification);
//                modification = DataFactory.createData("UPD_MULTIPLE_TIMES", null, null);
//                modifications.set(index, modification);
//                diffMap.put(line.getKey(), modifications);
//            }
//        }
//    }

    private List<DiffRow> generateDiffWithPreviousCommit(RevCommit commit, String fileName, GitLocal gitLocal) {
        DiffEntry diff = gitLocal.getFileDiffWithPreviousCommit(commit, fileName);
        if (diff == null) {
            return new ArrayList<>();
        }
        String previousCommitFileContent = gitLocal.getPreviousCommitFileContent(diff);
        String currentCommitFileContent = gitLocal.getCurrentCommitFileContent(diff);
        return CompareUtils.getDiffChanges(previousCommitFileContent, currentCommitFileContent);
    }

    private void handleDiffEntry(Map<Integer, Integer> amountOfTimes, Map.Entry<Integer, List<Data>> diffsEntry) {
        Data modification = ModificationDataUtils.getModificationDataFromLineActions(diffsEntry.getValue());
        if (modification == null) {
            return;
        }
        if (modification instanceof Inserted) {
            amountOfTimes.put(diffsEntry.getKey(), 1);
        } else if (modification instanceof Updated) {
            handleUpdateEntry(amountOfTimes, diffsEntry);
        }
    }

    private void handleUpdateEntry(Map<Integer, Integer> amountOfTimes, Map.Entry<Integer, List<Data>> diffsEntry) {
        int times = amountOfTimes.get(diffsEntry.getKey()) != null ? amountOfTimes.get(diffsEntry.getKey()) : 0;
        times++;
        amountOfTimes.put(diffsEntry.getKey(), times);
    }
}
