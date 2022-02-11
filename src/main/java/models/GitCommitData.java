package models;

public class GitCommitData {
    GitAuthor author;
    GitAuthor commiter;
    String message;
    GitTree tree;
    String url;
    int comment_count;
    GitVerification verification;
}
