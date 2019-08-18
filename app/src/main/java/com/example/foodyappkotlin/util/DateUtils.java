package com.example.foodyappkotlin.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
    public static final String SHORT_DEFAULT_DATE_FORMAT = "dd/MM/yy";
    public static final String DEFAULT_SERVER_DATE_FORMAT = "dd-MM-yyyy";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm";
    public static final String DEFAULT_TIME_FORMAT2 = "HH:mm:ss";

    public static final String DEFAULT_DAY_FORMAT = "dd-MM";
    public static final String DEFAULT_DAY_FNE_FORMAT = "dd/MM";
    public static final String DEFAULT_DURATION_FORMAT = "MMMM, yyyy";
    public static final String DEFAULT_FULL_MONTH_FORMAT = "MMMM";
    public static final String CHECKOUT_DATE_FORMAT = "MMM dd yyyy";
    public static final String NOTIFICATION_DATE_FORMAT = "dd MMM yyyy";
    public static final String MONTH_YEAR_FORMAT = "MM/yyyy";

    public static final String DEFAULT_CONVERSATION_FORMAT = "dd-MM-yyyy HH:mm:ss";
    public static final String DEFAULT_TIME_DATE_FORMAT = "HH:mm:ss, dd-MM-yyyy";


    public static final String DEPART_TIME_FORMAT = "HH:mm dd-MM-yyyy";

    public static final String DEFAULT_ITEM_CHAT_FORMAT = "HH:mm, dd/MM/yyyy";

    public static final String SERVER_DEPARTURE_TIME_FORMAT = "dd/MM HH:mm";
    public static final String DEPARTURE_TIME_FORMAT = "dd-MM HH:mm";
    public static final String FULL_DATE_FORMAT = "EEEE, dd MMM yyyy";
    public static final int SECOND_MILLIS = 1000;
    public static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    public static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    public static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static Long getLongFromString(String dateTime, String format) {
        if (!TextUtils.isEmpty(dateTime)) {
            SimpleDateFormat formater = new SimpleDateFormat(format);
            try {
                return formater.parse(dateTime).getTime();
            } catch (ParseException e) {
            }
        }
        return 0L;
    }

    public static String convertStringServerToLocal(String date) {
        return getStringFromLong(getLongFromString(date, DEFAULT_SERVER_DATE_FORMAT), DEFAULT_DATE_FORMAT);
    }

    public static String getStringFromLong(long date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, new Locale("vi"));
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);
        return formatter.format(cal.getTime());
    }

    public static String getBeginningOfMonths(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return DateUtils.getStringFromLong(calendar.getTimeInMillis(), "dd-MM-yyyy");
    }

    public static String getEndOfMonths(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return DateUtils.getStringFromLong(calendar.getTimeInMillis(), "dd-MM-yyyy");
    }

    public static Long getCurrentTime() {
        return new Date().getTime();
    }

    public static Long getSecondsCurrentTime(){
        return (getCurrentTime() / 1000);
    }

    public static String getFirstDateOfThisMonth(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Calendar date = Calendar.getInstance();
        date.set(Calendar.DAY_OF_MONTH, 1);
        return formatter.format(date.getTime());
    }

    public static String getFirstDateOfLastMonth(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MONTH, -1);
        date.set(Calendar.DATE, 1);
        return formatter.format(date.getTime());
    }

    public static String getLastDateOfLastMonth(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MONTH, -1);
        date.set(Calendar.DATE, date.getActualMaximum(Calendar.DAY_OF_MONTH));
        return formatter.format(date.getTime());
    }

    public static String getLastDateOfNextMonth(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MONTH, 1);
        date.set(Calendar.DATE, date.getActualMaximum(Calendar.DAY_OF_MONTH));
        return formatter.format(date.getTime());
    }

    public static boolean isToday(long date) {
        return android.text.format.DateUtils.isToday(date);
    }

    public static boolean isYesterday(long date) {
        return android.text.format.DateUtils.isToday(date + android.text.format.DateUtils.DAY_IN_MILLIS);
    }

    public static boolean isThisYear(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return Calendar.getInstance().get(Calendar.YEAR) == calendar.get(Calendar.YEAR);
    }

    public static String convertMinuteToHours(Long minute) {
        Long hours = minute / 60; //since both are ints, you get an int
        Long minutes = minute % 60;
        StringBuilder builderTime = new StringBuilder();
        if (hours < 10) {
            builderTime.append("0");
            builderTime.append(hours);
        } else {
            builderTime.append(hours);
        }
        builderTime.append(":");
        if (minutes < 10) {
            builderTime.append("0");
            builderTime.append(minutes);
        } else {
            builderTime.append(minutes);
        }
        return builderTime.toString();
    }
}
