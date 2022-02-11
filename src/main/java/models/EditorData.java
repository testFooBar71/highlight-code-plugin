package models;

import com.intellij.openapi.editor.Editor;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.List;
import java.util.Map;

public class EditorData {
    Editor editor;
    Map<Integer, List<Data>> changes;
    boolean isActive;
    RevCommit sourceCommit;
    RevCommit destinationCommit;

    public EditorData(Map<Integer, List<Data>> changes, boolean isActive, RevCommit sourceCommit, RevCommit destinationCommit) {
        this.changes = changes;
        this.isActive = isActive;
        this.sourceCommit = sourceCommit;
        this.destinationCommit = destinationCommit;
    }

    public RevCommit getSourceCommit() {
        return sourceCommit;
    }

    public RevCommit getDestinationCommit() {
        return destinationCommit;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Map<Integer, List<Data>> getChanges() {
        return changes;
    }

    public void setChanges(Map<Integer, List<Data>> changes) {
        this.changes = changes;
    }

    public void setSourceCommit(RevCommit sourceCommit) {
        this.sourceCommit = sourceCommit;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    public Editor getEditor() {
        return editor;
    }
}
