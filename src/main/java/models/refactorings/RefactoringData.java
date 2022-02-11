package models.refactorings;

import models.Data;
import org.eclipse.jgit.lib.PersonIdent;

import java.time.LocalDateTime;

public abstract class RefactoringData extends Data {

    public RefactoringData() {
        this.author = null;
        this.dateTime = null;

        this.isOnParent = true;
    }

    public abstract String renderData();
    public abstract String getType();

    protected String printAdditionalData() {
        String authorData = this.author != null ? "<b>Author username:</b> " + author.getName() + "<br>"
                + "<b>Author email:</b> " + author.getEmailAddress() + "<br>" : "";
        String datetimeData = this.dateTime != null ? "<b>Commit datetime:</b> "
                + getDateTimeString() + "<br>" : "";
        return "<b>Commit info</b><br>" + authorData + datetimeData;
    }

    public void setAuthor(PersonIdent author) {
        this.author = author;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

}
