package models.refactoringminer;

public class RefactoringMinerOutput {
    Commit[] commits;

    public Refactoring[] getRefactorings() {
        return commits[0].getRefactorings();
    }
}
