package models.refactoringminer;

public class Location {
    String filePath;
    int startLine;
    int endLine;
    int startColumn;
    int endColumn;
    String codeElementType;
    String description;
    String codeElement;

    public String getFilePath() {
        return filePath;
    }

    public int getStartLine() {
        return startLine;
    }

    public int getEndLine() {
        return endLine;
    }

    public String getCodeElementType() {
        return codeElementType;
    }

    public String getDescription() {
        return description;
    }
}
