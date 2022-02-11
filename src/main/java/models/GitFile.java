package models;

public class GitFile {
    String name;
    String path;
    String sha;
    long size;
    String url;
    String html_url;
    String git_url;
    String download_url;
    String type;

    public String getContent() {
        return content;
    }

    String content;
    String encoding;
    GitLinks _links;
}
