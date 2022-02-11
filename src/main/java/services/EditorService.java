package services;

import com.intellij.openapi.editor.Editor;
import editor.EditorUtils;
import models.Data;
import models.EditorData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditorService {
    private Map<String, EditorData> editors;

    public void initialize() {
        editors = new HashMap<>();
    }

    public boolean editorIsOnMap(Editor editor) {
        String path = EditorUtils.getRelativePath(editor);
        return editors.containsKey(path);
    }

    public void setEditorWithData(Editor editor, EditorData editorData) {
        String path = EditorUtils.getRelativePath(editor);
        editorData.setEditor(editor);
        editors.put(path, editorData);
    }

    public void setActiveEditor(Editor editor) {
        for (Map.Entry<String, EditorData> editorsEntry: editors.entrySet()) {
            EditorData editorData = editorsEntry.getValue();
            String path = EditorUtils.getRelativePath(editor);
            editorData.setActive(editorsEntry.getKey().equals(path));
            editors.put(editorsEntry.getKey(), editorData);
        }
    }

    public EditorData getEditorData(Editor editor) {
        String path = EditorUtils.getRelativePath(editor);
        return editors.get(path);
    }

    public EditorData getActiveEditorData() {
        return editors.values().stream().filter(EditorData::isActive).findFirst().get();
    }

    public String getActiveEditorPath() {
        return editors.entrySet()
                .stream()
                .filter(entry -> entry.getValue().isActive())
                .findFirst()
                .get()
                .getKey();
    }

    public Map<Integer, List<Data>> getActiveEditorChanges() {
        return editors.values()
                .stream()
                .filter(EditorData::isActive)
                .map(EditorData::getChanges)
                .findFirst().orElse(new HashMap<>());
    }
}
