package utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {
    public String parseDate(Date date) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return getDayOfMonth(dateTime) + "/" + getMonth(dateTime) + "/" +
                dateTime.getYear() + " " + getHour(dateTime) + ":" +
                getMinute(dateTime);

    }

    private String getDayOfMonth(LocalDateTime dateTime) {
        return dateTime.getDayOfMonth() >= 10 ? String.valueOf(dateTime.getDayOfMonth()) : "0" + dateTime.getDayOfMonth();
    }

    private String getMonth(LocalDateTime dateTime) {
        return dateTime.getMonthValue() >= 10 ? String.valueOf(dateTime.getMonthValue()) : "0" + dateTime.getMonthValue();
    }

    private String getHour(LocalDateTime dateTime) {
        return dateTime.getHour() >= 10 ? String.valueOf(dateTime.getHour()) : "0" + dateTime.getHour();
    }

    private String getMinute(LocalDateTime dateTime) {
        return dateTime.getMinute() >= 10 ? String.valueOf(dateTime.getMinute()) : "0" + dateTime.getMinute();
    }
}
