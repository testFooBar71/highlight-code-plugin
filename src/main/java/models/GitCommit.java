package models;

public class GitCommit {
    public String getSha() {
        return sha;
    }

    String sha;
    String node_id;
    GitCommitData commit;
    String url;
    String html_url;
    String comments_url;
    GitUser author;
    GitUser commiter;
    GitParentCommit[] parents;
}
