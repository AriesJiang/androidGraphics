package com.aries.graphics.pixelate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * TimeUtils
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-8-24
 */
public class TimeUtils {

    public static final int ONE_SEC_IN_MICROSEC = 1000;

    public static final SimpleDateFormat DEFAULT_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
    public static final SimpleDateFormat DATE_FORMATTER_DATE = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    public static final SimpleDateFormat DATE_FORMATTER_HOUR = new SimpleDateFormat("mm:ss", Locale.US);

    public static final int SECONDS_IN_DAY = 60 * 60 * 24;
    public static final long MILLIS_IN_DAY = 1000L * SECONDS_IN_DAY;

    public static final int NATIVE = 0;
    public static final int TODAY = 1;
    public static final int YESTODAY = 2; // 昨天
    public static final int THE_DAY_BEFORE_YESTERDA = 3; // 前天

    private static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE = "yyyy-MM-dd";
    private static final String TIME = "HH:mm:dd";
    private static final String YEAR = "yyyy";
    private static final String MONTH = "MM";
    private static final String DAY = "dd";
    private static final String HOUR = "HH";
    private static final String MINUTE = "mm";
    private static final String SEC = "ss";
    private static final String DATETIMECHINESE = "yyyy年MM月dd日 HH时mm分ss秒";
    private static final String DATECHINESE = "yyyy年MM月dd日";
    private static final String SIMPLEDATECHINESE = "MM月dd日";

    private TimeUtils() {
        throw new AssertionError();
    }

    public static boolean isSameDayOfMillis(final long ms1, final long ms2) {
        final long interval = Math.abs(ms1 - ms2);
        return (interval < MILLIS_IN_DAY)
                && (toDay(ms1) == toDay(ms2));
    }

    public static long toDay(long millis) {
        return (millis + TimeZone.getDefault().getOffset(millis)) / MILLIS_IN_DAY;
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMATTER}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMATTER);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMATTER}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }


    /**
     * yyyy-MM-dd HH:mm:ss 格式的时间
     */
    public static String getSimpleDate() {
        long currentTime = System.currentTimeMillis();
        return getSimpleDateByCurrentTime(currentTime);
    }

    /**
     * 通过格林威治时间 转成 yyyy-MM-dd HH:mm:ss 格式的时间
     */
    public static String getSimpleDateByCurrentTime(long currentTimeMillis) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = new Date(currentTimeMillis);
        return formatter.format(date);
    }

    /**
     * yyyy-MM-dd HH:mm:ss 数组 [yyyy,mm,dd,hh,mm,ss] 格式转换 1300000000000
     */
    public static long getCurrentTimeMillisBySimpleDate(String... strings) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Integer.valueOf(strings[0]), Integer.valueOf(strings[1]), Integer.valueOf(strings[2]),
                Integer.valueOf(strings[3]), Integer.valueOf(strings[4]), Integer.valueOf(strings[5]));
        TimeZone tz = TimeZone.getTimeZone("GMT");
        calendar.setTimeZone(tz);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取今天日期，如2016-10-25
     */
    public static String getTodayDate() {
        SimpleDateFormat sdf = getDateFormat();
        return sdf.format(new Date());
    }

    /**
     * 获取通用的日期格式
     */
    public static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    /**
     * 计算两个时间差
     *
     * @param startTime
     * @param endTime
     * @return long
     * @throws ParseException
     */
    public static long calculateIntervalTime(String startTime, String endTime)
            throws ParseException {
        return parseDateTime(endTime).getTime()
                - parseDateTime(startTime).getTime();
    }

    // 字符串转换成时间
    public static Date parseDateTime(String datetime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DATETIME);
        return sdf.parse(datetime);
    }

    // 获取当前详细日期时间
    public static String getDateTime() {
        return new SimpleDateFormat(DATETIME).format(new Date());
    }

    // 转换为中文时间
    public static String getChineseDateTime() {
        return new SimpleDateFormat(DATETIMECHINESE).format(new Date());
    }

    // 转换为中文时间
    public static String getChineseDate() {
        return new SimpleDateFormat(DATECHINESE).format(new Date());
    }

    // 转换为中文时间
    public static String getSimpleChineseDate() {
        return new SimpleDateFormat(SIMPLEDATECHINESE).format(new Date());
    }

    // 获取系统默认的日期
    public static String getSimpledateDefaultDate() {
        return new SimpleDateFormat(DATE, Locale.getDefault()).format(new Date());
    }

    // 获取当前时间
    public static String getTime() {
        return new SimpleDateFormat(TIME).format(new Date());
    }

    // 获取当前年
    public static String getYear() {
        return new SimpleDateFormat(YEAR).format(new Date());
    }

    // 获取当前月
    public static String getMonth() {
        return new SimpleDateFormat(MONTH).format(new Date());
    }

    // 获取当前日
    public static String getDay() {
        return new SimpleDateFormat(DAY).format(new Date());
    }

    // 获取当前时
    public static String getHour() {
        return new SimpleDateFormat(HOUR).format(new Date());
    }

    // 获取当前分
    public static String getMinute() {
        return new SimpleDateFormat(MINUTE).format(new Date());
    }

    // 获取当前秒
    public static String getSec() {
        return new SimpleDateFormat(SEC).format(new Date());
    }

    // 获取昨天日期
    public static String getYestday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date d = cal.getTime();
        return new SimpleDateFormat(DATETIME).format(d);// 获取昨天日期
    }

    public static String getMonday() {
        Calendar calendar = new GregorianCalendar();
        // 取得本周一
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return new SimpleDateFormat(DATETIME).format(calendar.getTime());// 获取昨天日期
    }

    /**
     * 格式化成00:00这样的时间
     *
     * @param duration
     * @return
     */
    public static String durationToTimeFormat(long duration) {
        if (duration == -1) {
            return "";
        }
        long hours = TimeUnit.MILLISECONDS.toHours(duration);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
        String result;
        if (hours != 0 && seconds != 0) {
            result = String.format("%2d:%2d:%02d", hours, minutes, seconds);
        } else if (minutes == 0 && seconds == 0) {
            // 不足1s的都算1s
            result = "00:00";
        } else {
            result = String.format("%02d:%02d", minutes, seconds);
        }
        return result;
    }
}
