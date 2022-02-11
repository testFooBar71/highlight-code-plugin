package services;

import org.eclipse.jgit.lib.Repository;

public class GitService {
    private String remoteUrl;
    private String repoOwner;
    private String repoName;
    Repository repository;

    public String getLatestCommitHash() {
        return latestCommitHash;
    }

    public void setLatestCommitHash(String latestCommitHash) {
        this.latestCommitHash = latestCommitHash;
    }

    private String latestCommitHash;

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public void setRepoOwner(String repoOwner) {
        this.repoOwner = repoOwner;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public Repository getRepository() {
        return repository;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public String getRepoOwner() {
        return repoOwner;
    }

    public String getRepoName() {
        return repoName;
    }
}
