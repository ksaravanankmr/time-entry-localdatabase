package com.techno.timeentry.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {

    public static final int MODE_INSERT = 0, MODE_UPDATE = 1;

    public static final String TIME_FORMAT = " hh : mm ";
    public static final String DATE_FORMAT = " dd-mm-yyyy ";

    public static String getFormattedDateTime(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

}
