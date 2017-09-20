package com.youshibi.app.util;

import android.app.AlarmManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * 作者: 赵成柱 on 2016/8/19 0019.
 */
public class DateUtil {

    /**
     * 获取今天零点零分零秒的毫秒数
     */
    public static long getDayStartTimeMillis() {
        return System.currentTimeMillis() / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();
    }


    /**
     * 获取今天23点59分59秒的毫秒数
     */
    public static long getDayEndTimeMillis() {
        return getDayStartTimeMillis() + 24 * 60 * 60 * 1000 - 1;
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss 格式的时间字符串
     * 转换成时间戳
     */
    public static long timeToTimestamp(String dateTime) {
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long time = -1;
        try {
            time = format2.parse(dateTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    // 获得当前日期与本周一相差的天数
    private static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
    }

    /**
     * 获取本周第一天的零点零分零秒的毫秒数
     */
    public static long getWeekStartTimeMillis() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return timeToTimestamp(format.format(currentDate.getTime()) + " 00:00:00");
    }

    /**
     * 获取本周最后一天的23点59分59秒的毫秒数
     */
    public static long getWeekEndTimeMillis() {
        int mondayPlus = getMondayPlus();
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return timeToTimestamp(format.format(currentDate.getTime()) + " 23:59:59");
    }

    /**
     * 获取本月第一天的零点零分零秒的毫秒数
     */
    public static long getMondayStartTimeMillis() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return timeToTimestamp(format.format(cal.getTime()) + " 00:00:00");
    }

    /**
     * 获取本月最后一天的23点59分59秒的毫秒数
     */
    public static long getMondayEndTimeMillis() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return timeToTimestamp(format.format(cal.getTime()) + " 23:59:59");
    }

    /**
     * 时间友好显示
     * 刚刚-%s分钟前-%s小时前-昨天-前天-%s天前
     */
    public static String formatSomeAgo(long timeInMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        Calendar mCurrentDate = Calendar.getInstance();
        long crim = mCurrentDate.getTimeInMillis(); // current
        long trim = calendar.getTimeInMillis(); // target
        long diff = crim - trim;

        int year = mCurrentDate.get(Calendar.YEAR);
        int month = mCurrentDate.get(Calendar.MONTH);
        int day = mCurrentDate.get(Calendar.DATE);

        if (diff < 60 * 1000) {
            return "刚刚";
        }
        if (diff >= 60 * 1000 && diff < AlarmManager.INTERVAL_HOUR) {
            return String.format("%s分钟前", diff / 60 / 1000);
        }
        mCurrentDate.set(year, month, day, 0, 0, 0);
        if (trim >= mCurrentDate.getTimeInMillis()) {
            return String.format("%s小时前", diff / AlarmManager.INTERVAL_HOUR);
        }
        mCurrentDate.set(year, month, day - 1, 0, 0, 0);
        if (trim >= mCurrentDate.getTimeInMillis()) {
            return "昨天";
        }
        mCurrentDate.set(year, month, day - 2, 0, 0, 0);
        if (trim >= mCurrentDate.getTimeInMillis()) {
            return "前天";
        }
        if (diff < AlarmManager.INTERVAL_DAY * 30) {
            return String.format("%s天前", diff / AlarmManager.INTERVAL_DAY);
        }
        if (diff < AlarmManager.INTERVAL_DAY * 30 * 12) {
            return String.format("%s月前", diff / (AlarmManager.INTERVAL_DAY * 30));
        }
        return String.format("%s年前", mCurrentDate.get(Calendar.YEAR) - calendar.get(Calendar.YEAR));
    }

}
