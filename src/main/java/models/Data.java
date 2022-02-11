package models;

import org.eclipse.jgit.lib.PersonIdent;

import java.time.LocalDateTime;

public abstract class Data {
    protected long startOffset;
    protected long endOffset;
    protected PersonIdent author;
    protected LocalDateTime dateTime;
    protected boolean isOnParent;

    public abstract String renderData();
    public abstract String getType();
//    public abstract void setAdditionalData(String... additionalData);

    public void setAuthor(PersonIdent author) {
        this.author = author;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public long getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(long startOffset) {
        this.startOffset = startOffset;
    }

    public long getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(long endOffset) {
        this.endOffset = endOffset;
    }

    public void setOnParent(boolean isOnParent) {
        this.isOnParent = isOnParent;
    }

    public String getDateTimeString() {
        return getDayOfMonth() + "/" + getMonth() + "/" +
                dateTime.getYear() + " " + getHour() + ":" +
                getMinute();
    }

    private String getMinute() {
        return dateTime.getMinute() >= 10 ? String.valueOf(dateTime.getMinute()) : "0" + dateTime.getMinute();
    }

    private String getHour() {
        return dateTime.getHour() >= 10 ? String.valueOf(dateTime.getHour()) : "0" + dateTime.getHour();
    }

    private String getMonth() {
        return dateTime.getMonthValue() >= 10 ? String.valueOf(dateTime.getMonthValue()) : "0" + dateTime.getMonthValue();
    }

    private String getDayOfMonth() {
        return dateTime.getDayOfMonth() >= 10 ? String.valueOf(dateTime.getDayOfMonth()) : "0" + dateTime.getDayOfMonth();
    }
}
