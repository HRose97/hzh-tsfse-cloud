package com.hzh.common.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Hou Zhonghu
 * @since 2022/3/17 10:20
 */
public class TimeUtil {
    private final static Logger log = LoggerFactory.getLogger(TimeUtil.class);

    /**
     * 取指定日期时间的时间戳
     * @param 	sDate		String		日期
     * @param 	sTime		String		时间
     * return
     */
    public long timeMillis(String sDate, String sTime)
    {
        SimpleDateFormat sdf	=	new SimpleDateFormat("yyyyMMdd HHmmss");
        try {
            Date date			=	sdf.parse(sDate+" "+sTime);
            return date.getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            log.error("日期时间格式不符合：yyyymmdd HHmmss");
            return -1l;
        }
    }

    /**
     * 日期格式字符串转换成时间戳
     *
     * @param dateStr 字符串日期
     * @param format  如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public String date2TimeStamp(String dateStr, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(dateStr).getTime());
        } catch (Exception e) {
            log.error("日期格式转换异常");
        }
        return null;
    }

    /**
     * 时间戳转成相应格式的时间
     */
    public String timeFormat(String format, String timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(timeStamp)));
    }

    /**
     * 取当前时间点值
     * @param dateFormat
     * @return
     */
    public String getNowTime(String dateFormat){
        SimpleDateFormat sdf  =   new SimpleDateFormat(dateFormat);
        Date date             =   new Date();
        return sdf.format(date).length()<6 ? "0"+sdf.format(date):sdf.format(date);
    }

    /**
     * 取当前时间戳
     * @return
     */
    public long curTimeStamp(){
        Date date			=   new Date();
        return date.getTime();
    }

    /**
     * 输出当前日期8位格式字符串（YYYYMMDD）
     * @return
     */
    public String curDateStr(String dateFormat){
        SimpleDateFormat dateFormatter 	= new SimpleDateFormat(dateFormat);
        Date dDate = new Date();
        return dateFormatter.format(dDate);
    }

    /**
     * 取从当前算起过去的balance天的日期
     * @param balance
     * @return
     */
    public String getSpecifiedDay(int balance) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyyMMdd").parse(curDateStr("yyyyMMdd"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - balance);

        String specifieDay = new SimpleDateFormat("yyyyMMdd").format(c.getTime());
        return specifieDay;
    }

    /**
     * 返回过去指定分钟的时点值
     * @param balance
     * @return
     */
    public String getSpecifiedMinute(int balance){
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("HHmmss").parse(getNowTime("HHmmss"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int time = c.get(Calendar.MINUTE);
        c.set(Calendar.MINUTE, time - balance);

        String specifieDay = new SimpleDateFormat("HHmmssSSS").format(c.getTime());
        return specifieDay;
    }

    /**
     * 返回过去指定秒的时点值
     * @param balance
     * @return
     */
    public String getSpecifiedSecond(int balance){
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("HHmmss").parse(getNowTime("HHmmss"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int time = c.get(Calendar.SECOND);
        c.set(Calendar.SECOND, time - balance);

        String specifieDay = new SimpleDateFormat("HHmmssSSS").format(c.getTime());
        return specifieDay;
    }

    /**
     * 取前时间是在一天中的第几分钟
     * @return
     */
    public String getMinInDay(){
        int hour    = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute  = Calendar.getInstance().get(Calendar.MINUTE);
        int result  =   hour*60+minute;
        //return LocalDateTime.now().getHour()*60+LocalDateTime.now().getMinute()+"";
        return result+"";
    }

    /**
     * 判断是否是0时0分0秒
     * @return
     */
    public boolean equals(String strStart,LocalDateTime strEnd){
        LocalDateTime start = LocalDateTime.parse(strStart);
        strEnd = strEnd.withHour(0).withMinute(0).withSecond(0).withNano(0);
        return strEnd.equals(start);
    }

    /**
     * 判断是否是同一天，不相同则返回true
     * @return
     */
    public boolean equalsDay(String strStart,LocalDateTime strEnd){
        LocalDateTime start = LocalDateTime.parse(strStart);
        return strEnd.getDayOfYear() != start.getDayOfYear();
    }

    /**
     * 获取当天的前一天日期(YYYY.MM.DD)
     * @return
     */
    public String getTheDayBefore(int iDay, String strPattern){
        return LocalDate.now().plusDays(iDay).format(DateTimeFormatter.ofPattern(strPattern));
    }

    /**
     * 比较两个时间的时间差
     * @return
     */
    public long compareTime(String strBegin, String strEnd){
        String time1 = strBegin.trim().replace(" ", "T");
        String time2 = strEnd.trim().replace(" ", "T");
        Duration duration = Duration.between(LocalDateTime.parse(time1),LocalDateTime.parse(time2));
        return duration.toMillis();
    }

    /**
     * 取当前时间
     * @return
     */
    public String getCurTime(){
        SimpleDateFormat dateFormatter 	= new SimpleDateFormat("HHmmss");
        Date dDate = new Date();
        return dateFormatter.format(dDate);
    }

    /**
     * 取当前时间
     * @return
     */
    public String getCurTime(String timeFormat){
        SimpleDateFormat dateFormatter 	= new SimpleDateFormat(timeFormat);
        Date dDate = new Date();
        return dateFormatter.format(dDate);
    }

    /**
     * 取当前星期（周一开始）,星期天是0
     * @return
     */
    public String getCurWeek(){
        Calendar calendar   =   Calendar.getInstance();
        int week    =   calendar.get(Calendar.DAY_OF_WEEK)-1;
        return week+"";
    }

    /**
     * 时间戳（毫秒）转LocalDateTime
     * @param Millis
     * @return
     */
    public LocalDateTime toLocalDateTime(Long Millis){
        return new Date(Millis).toInstant().atOffset(ZoneOffset.ofHours(8)).toLocalDateTime();
    }


    /**
     * 指定格式的时间转为LocalDateTime
     * @param time
     * @param format
     * @return
     */
    public LocalDateTime date2LocalDateTime(String time, String format){
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(time, df);
    }

}

