package models.refactoringminer;

public class Refactoring {
    String type;
    String description;
    Location[] leftSideLocations;
    Location[] rightSideLocations;

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Location[] getRightSideLocations() {
        return rightSideLocations;
    }
}
