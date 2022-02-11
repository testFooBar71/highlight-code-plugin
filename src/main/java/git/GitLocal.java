package git;

import at.aau.softwaredynamics.runner.util.GitHelper;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import editor.EditorUtils;
import org.apache.commons.lang.ObjectUtils;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathSuffixFilter;
import org.eclipse.jgit.util.io.NullOutputStream;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GitLocal {

    String repoPath;
    Repository repository;

    public GitLocal(String repoPath) {
        this.repoPath = repoPath;
    }

    public GitLocal(Repository repository) {
        this.repository = repository;
    }

    public void openRepository() {
        try {
            repository = GitHelper.openRepository(repoPath);
        } catch (IOException exception) {
            System.out.println("IOException");
        }
    }

    public Repository getRepository() {
        return repository;
    }

    public void closeRepository() {
        repository.close();
    }

    public List<RevCommit> getSelectedLatestCommitsAscendant(int numberOfCommits) {
        try {
            Collection<RevCommit> commits = GitHelper.getCommits(repository, "HEAD");
            List<RevCommit> commitsList = new ArrayList<>(commits);
            List<RevCommit> selectedCommits = commitsList.subList(0, numberOfCommits);
            Collections.reverse(selectedCommits);
            return selectedCommits;
        } catch (IOException e) {
            return null;
        }
    }

    public List<RevCommit> getSelectedLatestCommitsDescendant(int numberOfCommits) {
        try {
            Collection<RevCommit> commits = GitHelper.getCommits(repository, "HEAD");
            List<RevCommit> commitsList = new ArrayList<>(commits);
            return commitsList.subList(0, numberOfCommits);
        } catch (IOException e) {
            return null;
        }
    }

    public List<RevCommit> getSelectedLatestCommitsDescendantFrom(RevCommit commit, int numberOfCommits) {
        try {
            Collection<RevCommit> commits = GitHelper.getCommits(repository, "HEAD");
            List<RevCommit> commitsList = new ArrayList<>(commits);
            int index = commitsList.indexOf(commit) + 1;
            return commitsList.subList(index, index + numberOfCommits);
        } catch (IOException e) {
            return null;
        }
    }

    public RevCommit getLatestCommit() {
        try {
            Collection<RevCommit> commits = GitHelper.getCommits(repository, "HEAD");
            List<RevCommit> commitsList = new ArrayList<>(commits);
            return commitsList.get(0);
        } catch (IOException exception) {
            return null;
        }
    }

    public RevCommit getLatestCommit(Repository repository) {
        try {
            Collection<RevCommit> commits = GitHelper.getCommits(repository, "HEAD");
            List<RevCommit> commitsList = new ArrayList<>(commits);
            return commitsList.get(0);
        } catch (IOException exception) {
            return null;
        }
    }

    public DiffEntry getFileDiffWithPreviousCommit(RevCommit commit, String fileName) {
        DiffFormatter diffFormatter = createDiffFormatter(fileName);
        RevCommit[] parents = commit.getParents().length > 0 ? commit.getParents() : new RevCommit[]{ null };
        try {
            List<DiffEntry> diffs = diffFormatter.scan(parents[0], commit.getTree());
            if (diffs.size() == 0) {
                return null;
            }
            return diffs.get(0);
        } catch (IOException exception) {
            return null;
        }
    }

    public String getCurrentCommitFileContent(DiffEntry diffEntry) {
        try {
            return GitHelper.getFileContent(diffEntry.getNewId(), repository);
        } catch(IOException exception) {
            return "";
        }
    }

//    public String getFileContentOnCommit(Editor editor, RevCommit commit) {
//        String fileName = EditorUtils.getFileName(editor);
//        try {
//            DiffFormatter diffFormatter = createDiffFormatter(fileName);
//            DiffEntry diffEntry = getDiffEntry(diffFormatter, commit);
//            return GitHelper.getFileContent(diffEntry.getNewId(), repository);
//            //no va a funcionar si no se hicieron cambios
//        } catch(NullPointerException | IOException e) {
//            return null;
//        }
//    }

    public String getFileContentOnCommit(Editor editor, RevCommit commit) {
        String path = EditorUtils.getRelativePath(editor);
        try (TreeWalk treeWalk = TreeWalk.forPath(repository, path, commit.getTree())) {
            ObjectId blobId = treeWalk.getObjectId(0);
            try (ObjectReader objectReader = repository.newObjectReader()) {
                ObjectLoader objectLoader = objectReader.open(blobId);
                byte[] bytes = objectLoader.getBytes();
                return new String(bytes, StandardCharsets.UTF_8);
            } catch (IOException e) {
                return "";
            }
        } catch (IOException | NullPointerException e) {
            return "";
        }
    }

    public String getFileContentOnCommit(VirtualFile file, RevCommit commit, Project project) {
        String path = getFilePath(file, project);
        try (TreeWalk treeWalk = TreeWalk.forPath(repository, path, commit.getTree())) {
            ObjectId blobId = treeWalk.getObjectId(0);
            try (ObjectReader objectReader = repository.newObjectReader()) {
                ObjectLoader objectLoader = objectReader.open(blobId);
                byte[] bytes = objectLoader.getBytes();
                return new String(bytes, StandardCharsets.UTF_8);
            } catch (IOException e) {
                return "";
            }
        } catch (IOException | NullPointerException e) {
            return "";
        }
    }

    private String getFilePath(VirtualFile file, Project project) {
        String filePath = file.getPath();
        String projectPath = project.getBasePath();
        return filePath.replaceAll(projectPath + "/", "");
    }


    private DiffFormatter createDiffFormatter(String fileName) {
        DiffFormatter diffFormatter = new DiffFormatter(NullOutputStream.INSTANCE);
        diffFormatter.setRepository(repository);
        diffFormatter.setPathFilter(PathSuffixFilter.create(fileName));
        diffFormatter.setDetectRenames(true);
        return diffFormatter;
    }

    public String getPreviousCommitFileContent(Editor editor) {
        String fileName = EditorUtils.getFileName(editor);
        try {
            Collection<RevCommit> commits = GitHelper.getCommits(repository, "HEAD");
            RevCommit commit = commits.iterator().next();
            DiffFormatter diffFormatter = createDiffFormatter(fileName);
            return getPreviousCommitContent(diffFormatter, commit);
        } catch(Exception e) {
            return "";
        }
    }

    public DiffEntry getDiffEntry(Editor editor) {
        String fileName = EditorUtils.getFileName(editor);
        try {
            Collection<RevCommit> commits = GitHelper.getCommits(repository, "HEAD");
            RevCommit commit = commits.iterator().next();
            DiffFormatter diffFormatter = createDiffFormatter(fileName);
            return getDiffEntry(diffFormatter, commit);
        } catch(Exception e) {
            return null;
        }
    }

    public DiffEntry getDiffEntryOfCommit(RevCommit commit, Editor editor) {
        String fileName = EditorUtils.getFileName(editor);
        try {
            DiffFormatter diffFormatter = createDiffFormatter(fileName);
            return getDiffEntry(diffFormatter, commit);
        } catch(Exception e) {
            return null;
        }
    }

    public String getPreviousCommitFileContent(DiffEntry diffEntry) {
        try {
            return GitHelper.getFileContent(diffEntry.getOldId(), repository);
        } catch (IOException exception) {
            return "";
        }
    }

    private DiffEntry getDiffEntry(DiffFormatter diffFormatter, RevCommit commit) {
        RevCommit[] parents = commit.getParents();
        try {
            if (parents.length != 0) {
                List<DiffEntry> diffs = diffFormatter.scan(parents[0], commit.getTree());
                if (diffs.size() == 0) {
                    return null;
                }
                return diffs.get(0);
            }
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    private String getPreviousCommitContent(DiffFormatter diffFormatter, RevCommit commit) {
        RevCommit[] parents = commit.getParents();
        try {
            if (parents.length != 0) {
                List<DiffEntry> diffs = diffFormatter.scan(parents[0], commit.getTree());
                DiffEntry diff = diffs.get(0);
                return GitHelper.getFileContent(diff.getOldId(), repository);
            }
            return "";
        } catch (IOException e) {
            return "";
        }
    }
}
