package models;

import com.intellij.openapi.editor.Editor;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.List;
import java.util.Map;

public class JavaFileData {
    Map<Integer, List<Data>> changes;
    boolean isNewFile;

    public JavaFileData(Map<Integer, List<Data>> changes, boolean isNewFile) {
        this.changes = changes;
        this.isNewFile = isNewFile;
    }

    public JavaFileData(Map<Integer, List<Data>> changes) {
        this.changes = changes;
        this.isNewFile = false;
    }

    public Map<Integer, List<Data>> getChanges() {
        return changes;
    }

    public void setChanges(Map<Integer, List<Data>> changes) {
        this.changes = changes;
    }

    public boolean isNewFile() {
        return isNewFile;
    }

    public void setNewFile(boolean newFile) {
        isNewFile = newFile;
    }

    public int countChanges() {
        int numberOfChanges = 0;
        for (Map.Entry<Integer, List<Data>> change: changes.entrySet()) {
            numberOfChanges += change.getValue().size();
        }
        return numberOfChanges;
    }
}
