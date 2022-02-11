package models.actions;

import models.DiffRow;
import models.actions.ModificationData;
import org.eclipse.jgit.lib.PersonIdent;

import java.time.LocalDateTime;

public class Updated extends ModificationData {

    String previousContent;

    public Updated(PersonIdent author, LocalDateTime commitDate, long startOffset, long endOffset, String... additionalData) {
        super();
        this.author = author;
        this.dateTime = commitDate;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.previousContent = additionalData[0];
    }

    @Override
    public void setAdditionalData(String... additionalData) {
        this.previousContent = "temp";
    }

    @Override
    public String renderData() {
        return isOnParent ? "<b>UPDATED<br>Previous line content:</b><br>" + previousContent + "<br>" +
                "<b>Commit info<br>Author username:</b> " + author.getName() + "<br>" +
                "<b>Author email:</b> " + author.getEmailAddress() + "<br>" +
                "<b>Commit datetime:</b> " + getDateTimeString() + "<br>" :
                "This was modified before<br>" + getDateTimeString();
    }

    @Override
    public String getType() {
        return "UPD";
    }

    private String printPreviousContent() {
        return previousContent.length() > 70 ? previousContent.substring(0, 70) + "..." : previousContent;
    }
}
