package models.actions;

import models.DiffRow;
import models.actions.ModificationData;
import org.eclipse.jgit.lib.PersonIdent;

import java.time.LocalDateTime;

public class Moved extends ModificationData {
    int previousLine;

    public Moved(PersonIdent author, LocalDateTime commitDate, long startOffset, long endOffset, String... additionalData) {
        super();
        this.author = author;
        this.dateTime = commitDate;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.previousLine = Integer.parseInt(additionalData[0]);
    }

    @Override
    public void setAdditionalData(String... additionalData) {
//        this.previousLine = Integer.parseInt(additionalData[0]);
        this.previousLine = 2;
    }

    @Override
    public String renderData() {
        return isOnParent ? "<b>MOVED<br>Previously in line: </b>" + previousLine +
                "<br><b>Commit info<br>Author username:</b> " + author.getName() + "<br>" +
                "<b>Author email:</b> " + author.getEmailAddress() + "<br>" +
                "<b>Commit datetime:</b> " + getDateTimeString() + "<br>" :
                "This was modified before<br>" + getDateTimeString();
    }

    @Override
    public String getType() {
        return "MOV";
    }
}
