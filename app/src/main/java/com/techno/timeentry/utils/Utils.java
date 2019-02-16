package com.techno.timeentry.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {

    public static final int MODE_INSERT = 0, MODE_UPDATE = 1;

    public static final String TIME_FORMAT = " hh : mm ";
    public static final String DATE_FORMAT = " dd-MM-yyyy ";

    public static String getFormattedDateTime(long milliSeconds, String format) {
        if(format.equals(TIME_FORMAT) && milliSeconds <=0 ) {
            return " 00 : 00 ";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

}
