package models;

public class DiffRow {
    String commit;
    String label;

    public String getCommit() {
        return commit;
    }

    public String getLabel() {
        return label;
    }

    public String getChange() {
        return change;
    }

    public String getNodeType() {
        return nodeType;
    }

    public int getSrcStart() {
        return srcStart;
    }

    public int getSrcEnd() {
        return srcEnd;
    }

    public int getDstStart() {
        return dstStart;
    }

    public int getDstEnd() {
        return dstEnd;
    }

    public String getDstFile() {
        return dstFile;
    }

    String change;
    String nodeType;
    int srcStart;
    int srcEnd;
    int dstStart;
    int dstEnd;
    String dstFile;

    public DiffRow(String commit, String label, String change, String nodeType, int srcStart, int srcEnd, int dstStart, int dstEnd, String dstFile) {
        this.commit = commit;
        this.label = label;
        this.change = change;
        this.nodeType = nodeType;
        this.srcStart = srcStart;
        this.srcEnd = srcEnd;
        this.dstStart = dstStart;
        this.dstEnd = dstEnd;
        this.dstFile = dstFile;
    }

    public String toString() {
        return this.commit + ", " + this.label + ", " + this.change + ", " + this.nodeType + ", " + this.srcStart + ", " + this.srcEnd + ", " + this.dstStart + ", " + this.dstEnd + ", " + this.dstFile;
    }
}
