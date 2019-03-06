package com.techno.timeentry.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static final int MODE_INSERT = 0, MODE_UPDATE = 1, MODE_ADD = 2;

    public static final String TIME_FORMAT = " HH : mm ";
    public static final String DATE_FORMAT = " dd-MM-yyyy ";

    public static String getFormattedDateTime(long milliSeconds, String format) {
        if (format.equals(TIME_FORMAT) && milliSeconds <= 0) {
            return " 00 : 00 ";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public static String getFormattedTimeSpent(long timeInMilliSecs) {

        long hours = TimeUnit.MILLISECONDS.toHours(timeInMilliSecs);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMilliSecs) % 60;

        String timeSpent = " ";

        if (hours > 1)
            timeSpent = hours + " Hrs ";
        else if (hours > 0)
            timeSpent = hours + " Hr ";


        if (minutes > 1)
            timeSpent += minutes + " mins ";
        else
            timeSpent += minutes + " min ";

        return timeSpent;
    }

    public static long getTimeSpent(long inTime, long outTime) {
        if (outTime > 0)
            return outTime - inTime;
        else
            return 0;
    }

}
