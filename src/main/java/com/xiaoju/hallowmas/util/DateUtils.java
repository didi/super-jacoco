package com.xiaoju.hallowmas.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtils extends PropertyEditorSupport {
    private static final Log logger = LogFactory.getLog(DateUtils.class);

    public static final String yyyy_MM_DD_HH_MM_SS_S = "yyyy-MM-dd HH:mm:ss.S";

    public static final String yyyy_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    public static final String yyyyMMDDHHMMSS = "yyyyMMDDHHMMSS";

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String DATE_MONTH_FORMAT = "yyyy-MM";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_DAY_FORMAT = "yyyyMMdd";

    public static final String MMxDDx_HHxmmx = "MM月dd日HH时mm分";

    public static final String YYYYxMMxDDx = "YYYY年MM月dd日";

    public static final String MM_DD = "MM/dd";

    public static final String MM_DD_HH_mm = "M 月 dd 日 HH:mm";

    public static final String HH_MM_ss = "HH:mm:ss";

    private String dateFormat = "yyyy-MM-dd";

    /**
     * 开始时间和结束时间间的间隔.
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 时间间隔 毫秒粒度
     * @deprecated
     */
    public static long timeIntervalInDay(Date start, Date end) {
        Date now = beginTimeOfDay(end);
        if (now == null || start == null) {
            return 0;
        }
        return Math.abs(now.getTime() - start.getTime());
    }

    /**
     * 开始时间和结束时间间的间隔. 可以为负数,单位毫秒
     *
     * @param start
     * @param end
     * @return
     */
    public static long getIntervalBetween(Date start, Date end) {
        return end.getTime() - start.getTime();
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        logger.debug("set Text");
        SimpleDateFormat frm = new SimpleDateFormat(dateFormat);
        try {
            Date date = frm.parse(text);
            this.setValue(date);
        } catch (Exception exp) {
            logger.error(exp);
        }

    }

    /**
     * 如果解析错误则返回null
     *
     * @param text
     * @return
     */
    public static Date parse(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        text = text.trim();
        SimpleDateFormat frm = new SimpleDateFormat(DATE_FORMAT);
        try {
            return frm.parse(text);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date parse(String text, String format) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        SimpleDateFormat frm = new SimpleDateFormat(format);
        try {
            return frm.parse(text);
        } catch (ParseException e) {
            logger.error(e);
            return null;
        }
    }

    public static String parse(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return format.format(date);
    }

    public static String parse(Date date, String format) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat fmat = new SimpleDateFormat(format);
        return fmat.format(date);
    }

    public static String parse(long timestamp, String format) {
        Timestamp ts = new Timestamp(timestamp);
        SimpleDateFormat fmat = new SimpleDateFormat(format);

        return fmat.format(ts);
    }

    /**
     * 把时间的时分秒设置为 23:59:59 999
     *
     * @param date
     * @return
     */
    public static Date endTimeOfDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    /**
     * 获取当前时间距离今天最后一毫秒秒的差值
     *
     * @return
     */
    public static long getDvalueNowToEnd() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.addDay(new Date(), 1));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date d = cal.getTime();
        return d.getTime() - (new Date()).getTime();
    }

    /**
     * 把时间的时分秒设置为当天的 23:59:59 ，去掉999毫秒的精度
     *
     * @param date
     * @return
     */
    public static Date endTimeOfDayForTask(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 把时间的时分秒设置为 0:0:0 0
     *
     * @param date
     * @return
     */
    public static Date beginTimeOfDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 在当前日期的基础上加减天数
     *
     * @param date
     * @param day
     * @return
     */
    public static Date addDay(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + day);
        return cal.getTime();
    }

    /**
     * 在当前日期基础上添加分钟数
     *
     * @param date
     * @param minutes
     * @return
     */
    public static Date addMinutes(Date date, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + minutes);
        return cal.getTime();
    }

    /**
     * 是否只包含日期部分，不包含时分秒部分
     *
     * @param input
     * @return
     */
    public static boolean isDate(String input) {
        if (StringUtils.isBlank(input)) {
            return false;
        }
        Matcher matcher = Pattern.compile("^(\\d{4})-(0\\d{1}|[1-9]|1[0-2])-([1-9]|0\\d{1}|[12]\\d{1}|3[01])$")
                .matcher(input);
        return matcher.matches();
    }

    public static boolean isDateTime(String input, String formt) {
        SimpleDateFormat df = new SimpleDateFormat(formt);
        try {
            df.parse(input);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static Date getDate(String date) {
        return getDate(date, yyyy_MM_DD_HH_MM_SS);
    }

    public static Date getDate(String date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        try {
            return df.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            logger.error(e);
        }

        return null;
    }

    public static int getDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static String getDate(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    /**
     * 指定时间的凌晨
     *
     * @param date
     * @return
     */
    public static Date getDateZero(Date date) {
        date = org.apache.commons.lang.time.DateUtils.setHours(date, 0);
        date = org.apache.commons.lang.time.DateUtils.setMinutes(date, 0);
        date = org.apache.commons.lang.time.DateUtils.setSeconds(date, 0);
        date = org.apache.commons.lang.time.DateUtils.setMilliseconds(date, 0);
        return date;
    }

    public static Calendar getDateZeroCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        return cal;
    }

    public static Date addMonth(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + i);
        return cal.getTime();
    }

    /**
     * 每周从星期一开始、星期天结束
     *
     * @param date
     * @return
     */
    public static int getDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if (day == 1)
            day = 7;
        else
            day--;
        return day;
    }

    /**
     * 每周从星期一开始、星期天结束
     *
     * @param date
     * @return
     */
    public static String getDayDescOfWeek(Date date) {
        int dayOfWeek = getDayOfWeek(date);
        String str = "";
        switch (dayOfWeek) {
            case 1:
                str = "星期一";
                break;
            case 2:
                str = "星期二";
                break;
            case 3:
                str = "星期三";
                break;
            case 4:
                str = "星期四";
                break;
            case 5:
                str = "星期五";
                break;
            case 6:
                str = "星期六";
                break;
            case 7:
                str = "星期天";
                break;
            default:
                break;
        }
        return str;
    }

    /**
     * 获取指定时间在一年当中所属第几周(每周从周一开始)
     *
     * @param date
     * @return 返回第几周. ie 201301 表示2013年第一周
     */
    public static int getYearWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);

        if (month == Calendar.DECEMBER && weekOfYear == 1 && day == Calendar.SUNDAY) {
            cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1);
            weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        } else if (month == Calendar.DECEMBER && weekOfYear == 1 && day != Calendar.SUNDAY) {
            year++;
        } else if (day == Calendar.SUNDAY) { // 星期天
            weekOfYear--;
        }

        String result = String.valueOf(year);
        if (weekOfYear < 10) {
            result += "0" + weekOfYear;
        } else {
            result += weekOfYear;
        }
        return Integer.parseInt(result);
    }

    /**
     * 每一周从星期天开始(西方习惯)
     *
     * @param date
     * @return
     * @see DateUtils#getYearWeek()
     */
    @Deprecated
    public static int getWeekOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 每一周从星期一开始(中国习惯)
     *
     * @param date
     * @return
     * @see DateUtils#getYearWeek()
     */
    @Deprecated
    public static int getWeekOfYearBeginOfMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if (day == 1) { // 星期天
            weekOfYear--;
        }
        return weekOfYear;
    }

    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH);
    }

    public static int getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getMinute(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MINUTE);
    }

    public static Date addDays(Date date, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, day);
        return cal.getTime();
    }

    /**
     * 月初第一天
     *
     * @param date
     * @return
     */
    public static Date beginOfMonth(Date date) {
        if (date == null)
            return date;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取指定日期所在周的周开始日期(每周从周一开始)
     *
     * @param date
     * @return
     */
    public static Date beginOfWeek(Date date) {
        int week = DateUtils.getDayOfWeek(date);
        Date beginOfWeek = null;
        if (week == 1) { // 星期一
            beginOfWeek = date;
        } else {
            beginOfWeek = DateUtils.addDay(date, -(week - 1));
        }
        beginOfWeek = DateUtils.beginTimeOfDay(beginOfWeek);
        return beginOfWeek;
    }

    /**
     * 获取指定日期所在周的周结束日期(每周从周一开始)
     *
     * @param date
     * @return
     */
    public static Date endOfWeek(Date date) {
        int week = DateUtils.getDayOfWeek(date);
        Date endOfWeek = null;
        if (week == 0) { // 星期一
            endOfWeek = date;
        } else {
            endOfWeek = DateUtils.addDay(date, (7 - week));
        }
        endOfWeek = DateUtils.beginTimeOfDay(endOfWeek);
        return endOfWeek;
    }

    /**
     * n为推迟的周数，0本周，-1向前推迟一周，1下周，依次类推
     *
     * @param n
     * @param weekDay
     * @return
     */
    public static String getWeekDay(int n, int weekDay) {
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DATE, n * 7);
        // 想周几，这里就传几Calendar.MONDAY（TUESDAY...）
        cal.set(Calendar.DAY_OF_WEEK, weekDay);
        return new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()) + " 00:00:00";
    }

    public static void recursionDay(Date begin, Date end, List<String> allDays) {
        if (begin.after(end)) {
            return;
        }

        Date b = DateUtils.beginTimeOfDay(begin);
        Date e = DateUtils.endTimeOfDay(begin);
        if (e.after(end)) {
            e = end;
        }

        allDays.add(DateUtils.parse(b));

        recursionDay(DateUtils.beginTimeOfDay(DateUtils.addDay(begin, 1)), end, allDays);

    }

    public static void recursionMonth(Date begin, Date end) {
        if (begin.after(end)) {
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(begin);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        Date e = cal.getTime();
        e = DateUtils.endTimeOfDay(e);
        if (e != null && e.after(end)) {
            e = end;
        }

        recursionMonth(DateUtils.beginTimeOfDay(DateUtils.addDay(e, 1)), end);
    }

    public static void recursionWeek(Date begin, Date end, List<Date> lastDayOfWeek) {
        if (begin.after(end)) {
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(begin);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        dayOfWeek = dayOfWeek == 1 ? 7 : dayOfWeek - 1;
        Date b = DateUtils.beginTimeOfDay(begin);

        Date e;
        if (dayOfWeek == 7) {
            e = DateUtils.endTimeOfDay(begin);
        } else {
            e = DateUtils.addDay(b, 7 - dayOfWeek);
            e = DateUtils.endTimeOfDay(e);
        }

        if (e != null && e.after(end)) {
            e = end;
        }

        lastDayOfWeek.add(e);

        recursionWeek(DateUtils.beginTimeOfDay(DateUtils.addDay(e, 1)), end, lastDayOfWeek);

    }

    public static void recursionWeek(Date begin, Date end, Map<Date, Date> weekDates) {
        begin = DateUtils.beginTimeOfDay(begin);
        end = DateUtils.beginTimeOfDay(end);
        if (begin != null && begin.after(end)) {
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(begin);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        dayOfWeek = dayOfWeek == 1 ? 7 : dayOfWeek - 1;
        Date b = begin;

        Date e;
        if (dayOfWeek == 7) {
            e = begin;
        } else {
            e = DateUtils.addDay(b, 7 - dayOfWeek);
        }

        if (e != null && (e.equals(end) || e.after(end))) {
            e = end;
        } else {
            e = DateUtils.addDay(e, 1);
        }

        weekDates.put(b, e);

        if (e != null && !e.equals(end)) {
            recursionWeek(e, end, weekDates);
        }

    }

    /**
     * 判断日期是否昨天 或者前天.....
     *
     * @param paramDate 要判断的时间
     * @param day       昨天传1 前天传2以此类推
     * @return
     * @throws ParseException
     */
    public static boolean judgeDay(Date paramDate, Integer day) throws ParseException {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = format.format(date);
        // 得到今天零时零分零秒这一时刻
        Date today = format.parse(todayStr);
        // 与今日零时零分零秒比较
        if ((today.getTime() - paramDate.getTime()) > (day - 1) * 86400000
                && (today.getTime() - paramDate.getTime()) < day * 86400000) {
            return true;
        }
        return false;
    }

    /**
     * 判断日期是否今天.....
     *
     * @param paramDate 要判断的时间
     * @return
     * @throws ParseException
     */
    public static boolean judgeToday(Date paramDate) throws ParseException {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = format.format(date);
        // 得到今天零时零分零秒这一时刻
        Date today = format.parse(todayStr);
        // 与今日零时零分零秒比较
        if ((paramDate.getTime() - today.getTime()) > 0 && (paramDate.getTime() - today.getTime()) < 86400000) {
            return true;
        }
        return false;
    }

    public static Date addSeconds(Date date, int seconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) + seconds);
        return cal.getTime();
    }

    public static Date addMinute(Date date, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) + minutes);
        return cal.getTime();
    }

    public static Date addHour(Date date, int hours) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR, cal.get(Calendar.HOUR) + hours);
        return cal.getTime();
    }

    /**
     * 把毫秒转换为 x小时x分 格式字符串
     *
     * @param millisecond
     * @return
     */
    public static String convertMillisecond(Long millisecond) {
        if (millisecond != null && millisecond > 0) {
            Double hm = formatTime(millisecond, TimeUnit.HOUR);
            int hour = hm.intValue();
            double minute = Math.abs(hm - hour);
            String rslt = (hour > 0 ? (hour + "小时") : "") + (minute > 0 ? ((int) (minute * 60) + "分") : "");
            if (StringUtils.isBlank(rslt))
                rslt = "0小时";
            return rslt;
        }
        return "0小时0分";
    }

    public static Double formatTime(long onlineTime, TimeUnit unit) {
        if (onlineTime <= 0)
            return 0.0;

        double time = 0;
        if (unit == TimeUnit.MINUTE)
            time = onlineTime / (1000.0 * 60.0); // 分
        else if (unit == TimeUnit.HOUR)
            time = onlineTime / (1000.0 * 60.0 * 60.0); // 小時
        DecimalFormat format = new DecimalFormat("0.00");
        return Double.parseDouble(format.format(time));
    }

    /**
     * 把日期格式为 x月x日
     *
     * @param day 格式为 20131015
     * @return
     */
    public static String getFormatedDay(int day) {
        // 20130916
        String dayStr = String.valueOf(day);
        String m = dayStr.substring(4, 6);
        String d = dayStr.substring(6, dayStr.length());
        return Integer.parseInt(m) + "月" + Integer.parseInt(d) + "日";
    }

    /**
     * 把日期格式为 x月x日
     *
     * @param day 日期对象
     * @return
     */
    public static String getFormatedDay(Date day) {
        // 20130916
        if (day == null) {
            return "";
        }
        int dayint = Integer.parseInt(parse(day, DATE_DAY_FORMAT));
        String dayStr = String.valueOf(dayint);
        String m = dayStr.substring(4, 6);
        String d = dayStr.substring(6, dayStr.length());
        return Integer.parseInt(m) + "月" + Integer.parseInt(d) + "日";
    }

    /**
     * 返回：今天、昨天、具体日期
     *
     * @param date
     * @param dayFormat
     * @return
     */
    public static String getSpecificDay(Date date, String dayFormat) {
        if (date == null) {
            return "";
        }
        if (StringUtils.isBlank(dayFormat)) {
            dayFormat = DATE_DAY_FORMAT;
        }
        String returnDayStr = "";
        String todayStr = DateUtils.parse(new Date(), dayFormat);
        Date yesterday = DateUtils.addDay(new Date(), -1);
        String yesterdayStr = DateUtils.parse(yesterday, dayFormat);
        String dayStr = DateUtils.parse(date, dayFormat);
        if (dayStr.equals(todayStr)) {
            returnDayStr = "今天";
        } else if (dayStr.equals(yesterdayStr)) {
            returnDayStr = "昨天";
        } else {
            returnDayStr = dayStr;
        }
        return returnDayStr;
    }

    /**
     * 获取星期一对应的日期 n为推迟的周数，0本周，-1向前推迟一周，1下周，依次类推
     *
     * @param n
     * @return
     */
    public static Date getMondoyByN(int n) {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, n * 7);

        return beginOfWeek(cal.getTime());
    }

    /**
     * 获取星期天对应的日期 n为推迟的周数，0本周，-1向前推迟一周，1下周，依次类推
     *
     * @param n
     * @return
     */
    public static Date getSundoyByN(int n) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, n * 7);

        return endOfWeek(cal.getTime());
    }

    public static Date addYear(Date date, int year) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + year);
        return cal.getTime();
    }

    public static int dayNum(Date startDate, Date endDate) {
        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("startDate can't after endDate");
        }
        String startDateStr = null, endDateStr = null;
        int dayNum = 0;
        while (true) {
            dayNum++;
            startDateStr = DateUtils.parse(startDate);
            endDateStr = DateUtils.parse(endDate);
            if (startDateStr.equals(endDateStr)) {
                return dayNum;
            }
            startDate = DateUtils.addDay(startDate, 1);
        }
    }

    /**
     * 查询两个时间间间隔了多少天
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int dayInterval(Date startDate, Date endDate) {
        if (startDate == null || endDate == null)
            return 0;
        long day1 = java.util.concurrent.TimeUnit.DAYS.toDays(startDate.getTime());
        long day2 = java.util.concurrent.TimeUnit.DAYS.toDays(endDate.getTime());
        long x = Math.abs(day1 - day2);
        int day = (int) (x / 86400000);
        return day;
    }
}
