package models.refactoringminer;

public class Commit {
    String repository;
    String sha1;
    String url;
    Refactoring[] refactorings;

    public Refactoring[] getRefactorings() {
        return refactorings;
    }
}
