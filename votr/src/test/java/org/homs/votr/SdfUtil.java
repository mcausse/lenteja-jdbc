package org.homs.votr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SdfUtil {

    private final String datePattern;

    public SdfUtil(String datePattern) {
        this.datePattern = datePattern;
    }

    public SdfUtil() {
        this("yyyyMMdd HH:mm:ss");
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
