package models.actions;

import models.DiffRow;
import org.eclipse.jgit.lib.PersonIdent;

import java.time.LocalDateTime;

public class Inserted extends ModificationData {
    String tempMetadata;

    public Inserted(PersonIdent author, LocalDateTime commitDate, long startOffset, long endOffset) {
        super();
        this.author = author;
        this.dateTime = commitDate;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    @Override
    public void setAdditionalData(String... additionalData) {
        this.tempMetadata = "temp";
    }

    @Override
    public String renderData() {
        return isOnParent ? "<b>INSERTED<br>Commit info<br>Author username:</b> " + author.getName() + "<br>" +
                "<b>Author email:</b> " + author.getEmailAddress() + "<br>" +
                "<b>Commit datetime:</b> " + getDateTimeString() + "<br>" :
                "This was modified before<br>" + getDateTimeString();
    }

    @Override
    public String getType() {
        return "INS";
    }
}
