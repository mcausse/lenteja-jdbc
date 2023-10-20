package org.homs.lechuga.queues.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    private final String datePattern;

    public DateUtil(String datePattern) {
        this.datePattern = datePattern;
    }

    public DateUtil() {
        this("yyyyMMdd HH:mm:ss");
    }

    public Date now() {
        return new Date();
    }

    public String fromDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        return sdf.format(date);
    }

    public Date toDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
