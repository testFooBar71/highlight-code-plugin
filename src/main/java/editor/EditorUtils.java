package editor;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.FileContentUtil;
import visualelements.VisualElementsUtils;

import java.util.ArrayList;
import java.util.List;

public class EditorUtils {

    public static String getFileName(Editor editor) {
        Document document = editor.getDocument();
        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(document);
        return virtualFile.getName();
    }

    public static String getRelativePath(Editor editor) {
        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(editor.getDocument());
        String filePath = virtualFile.getPath();
        Project project = editor.getProject();
        String projectPath = project.getBasePath();
        return filePath.replaceAll(projectPath + "/", "");
    }

    public static String getCurrentFileContent(Editor editor) {
        Document document = editor.getDocument();
        return document.getText();
    }

    public static void refreshEditor(Editor editor) {
        VirtualFile virtualFile = FileDocumentManager.getInstance().getFile(editor.getDocument());
        List<VirtualFile> files = new ArrayList<>();
        files.add(virtualFile);
        FileContentUtil.reparseFiles(editor.getProject(), files, true);
        new VisualElementsUtils().resetVisualElements(editor);
    }
}
