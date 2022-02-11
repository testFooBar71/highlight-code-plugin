package git;

import com.google.gson.Gson;
import com.intellij.openapi.editor.Editor;
import editor.EditorUtils;
import models.GitCommit;
import models.GitFile;
import services.GitService;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class GitRemote {
    HttpClient client;

    public GitRemote() {
        client = HttpClient.newHttpClient();
    }

    public String getRemoteUrlFromCmd(String projectPath) {
        final ProcessBuilder pBuilder = new ProcessBuilder("cmd.exe", "/c", "git config --get remote.origin.url");
        String myPath = projectPath.replace("/", "\\");
        pBuilder.directory(new File(myPath));
        try {
            final Process process = pBuilder.start();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            return reader.readLine();
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getRepoOwnerFromRemoteUrl(String remoteUrl) {
        return remoteUrl.split("/")[3];
    }

    public String getRepoNameFromRemoteUrl(String remoteUrl) {
        return remoteUrl.split("/")[4].replaceAll(".git", "");
    }

    public GitCommit[] getCommits(String gitApiUrl) {
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(gitApiUrl + "/commits"))
                .header("accept", "application/json")
                .build();
        HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
        Gson gson = new Gson();
        GitCommit[] commits;
        try {
            HttpResponse<String> response = client.send(request, bodyHandler);
            commits = gson.fromJson(response.body(), GitCommit[].class);
            return commits;
        } catch(InterruptedException | IOException exception) {
            return null;
        }
    }

    public String getPreviousCommitFileContent(String gitApiUrl, String commitSha, String filePath) {
        GitFile gitFile = getPreviousCommit(gitApiUrl, commitSha, filePath);
        String encodedContent = gitFile.getContent();
        String encodedWithoutLineBreaks = encodedContent.replaceAll("\n", "");
        byte[] decodedBytes = Base64.getDecoder().decode(encodedWithoutLineBreaks);
        String decodedContent = new String(decodedBytes);
        return decodedContent;
    }

    private GitFile getPreviousCommit(String gitApiUrl, String commitSha, String filePath) {
        HttpRequest request = HttpRequest.newBuilder(
                URI.create(gitApiUrl + "/contents" + filePath + "?ref=" + commitSha))
                .header("accept", "application/json")
                .build();
        HttpResponse.BodyHandler<String> bodyHandler = HttpResponse.BodyHandlers.ofString();
        Gson gson = new Gson();
        GitFile gitFile;
        try {
            HttpResponse<String> response = client.send(request, bodyHandler);
            gitFile = gson.fromJson(response.body(), GitFile.class);
            return gitFile;
        } catch(InterruptedException | IOException exception) {
            return null;
        }
    }

    public String getPreviousCommitFileContent(Editor editor) {
        String relativePath = EditorUtils.getRelativePath(editor);
        String githubApiUrl = getGithubApiUrl(editor);
        GitCommit[] commits = getCommits(githubApiUrl);
        String previousCommitSha = commits[1].getSha();
        return getPreviousCommitFileContent(githubApiUrl, previousCommitSha, relativePath);
    }

    private String getGithubApiUrl(Editor editor) {
        GitService gitService = editor.getProject().getService(GitService.class);
        String repoOwner = gitService.getRepoOwner();
        String repoName = gitService.getRepoName();
        return "https://api.github.com/repos/" + repoOwner + "/" + repoName;
    }

}
