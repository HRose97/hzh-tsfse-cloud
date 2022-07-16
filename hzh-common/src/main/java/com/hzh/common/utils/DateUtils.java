package com.hzh.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期封装类
 */
@Slf4j
public class DateUtils {
    /**
     * 锁对象
     */
    private static final Object lockObj = new Object();
    /**
     * 存放不同的日期模板格式的sdf的Map
     */
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();
    /**
     * 日期时间格式 *
     */
    public static final String timePattern = "HHmmss";
    public static final String timesPattern = "yyyy/MM/ddHH:mm:ss";
    public static final String datePattern = "yyyyMMdd";
    public static final String shortDatePattern = "yyMMdd";
    public static final String fullPattern = "yyyyMMddHHmmss";
    public static final String fullPatterns = "yyyyMMddHHmmssSS";
    public static final String partPattern = "yyMMddHHmmss";
    public static final String ticketPattern = "yyyy.MM.dd HH:mm:ss";
    public static final String settlePattern = "yyyy-MM-dd HH:mm:ss";
    public static final String hour_of_minute = "HHmm";
    public static final String timeColPattern = "HH:mm:ss";
    public static final String dateFullPattern = "yyyyMMdd HH:mm:ss";
    public static final String year_of_minute = "yyyyMMddHHmm";
    public static final String yearDate = "yyyy-MM-dd HH:mm";
    public static final String shotPattern = "yyyy-MM-dd";
    public static final String pointPattern = "yyyy.MM.dd";

    /**
     * 时间格式转换
     *
     * @param date          时间字符串
     * @param originPattern 原时间格式
     * @param targetPattern 新的时间格式
     * @return
     * @throws ParseException
     */
    public static String convert(String date, String originPattern, String targetPattern) throws ParseException {
        Date originDate = parse(date, originPattern);
        return format(originDate, targetPattern);
    }

    /**
     * 源日期和（目标日期加上毫秒数）比较大小， 大则返回false ，小返回true
     *
     * @param src    源日期
     * @param target 目的日期
     * @param second 秒数
     * @return 成功，失败
     */
    public static boolean compareDateForSecond(Date src, Date target, int second) {
        Calendar targetTime = Calendar.getInstance();
        targetTime.setTime(target);
        targetTime.add(Calendar.SECOND, second);
        Calendar srcTime = Calendar.getInstance();
        srcTime.setTime(src);
        return srcTime.compareTo(targetTime) <= 0;
    }
    /**
     * 原日期和目标日期比较 大则返回true
     *
     * @param src    原日期
     * @param target 目标日期
     * @return 返回结果
     */
    public static boolean compareDate(Date src, Date target) {
        Calendar targetTime = Calendar.getInstance();
        targetTime.setTime(target);
        Calendar srcTime = Calendar.getInstance();
        srcTime.setTime(src);
        return srcTime.compareTo(targetTime) >= 0;
    }

    public static String getCurrentAfter(int minute) {
        Calendar targetTime = Calendar.getInstance();
        targetTime.setTime(new Date());
        targetTime.add(Calendar.MINUTE, minute);
        return format(targetTime.getTime(), DateUtils.fullPattern);
    }

    public static String getCurrentAfter(int minute, String dataPattern) {
        Calendar targetTime = Calendar.getInstance();
        targetTime.setTime(new Date());
        targetTime.add(Calendar.MINUTE, minute);
        return format(targetTime.getTime(), dataPattern);
    }
    /**
     * 返回当前日期后的时间
     *
     * @param time  时间
     * @param field 时间格式 Calendar.SECOND MINUTE HOUR
     * @return 返回之后日期格式
     */
    public static Date getCurrentAfterTime(int time, int field) {
        Calendar targetTime = Calendar.getInstance();
        targetTime.setTime(new Date());
        targetTime.add(field, time);
        return targetTime.getTime();
    }

    /**
     * 获取当前时间
     * 格式：yyyyMMddHHmmss
     *
     * @return
     */
    public static String getCurrent(String pattern) {
        return format(new Date(), pattern);
    }

    /**
     * 获取当前时间
     * 格式：yyyy-MM-dd
     */
    public static String getCurrentFormat() {
        return format(new Date(), DateUtils.shotPattern);
    }

    /**
     * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
     *
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);
        // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
        if (tl == null) {
            synchronized (lockObj) {
                tl = sdfMap.get(pattern);
                if (tl == null) {
                    // 使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
                    tl = new ThreadLocal<SimpleDateFormat>() {
                        @Override
                        protected SimpleDateFormat initialValue() {
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sdfMap.put(pattern, tl);
                }
            }
        }

        return tl.get();
    }

    /**
     * 使用线程容器来获取SimpleDateFormat
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        return getSdf(pattern).format(date);
    }

    public static Date parse(String dateStr, String pattern) throws ParseException {
        return getSdf(pattern).parse(dateStr);
    }

    /**
     * 字符串类型日志格式转化为Date
     *
     * @param str
     * @return
     */
    public static Date stringToDate(String str) {
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
