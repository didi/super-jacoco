package com.xiaoju.hallowmas.util;

import java.math.BigDecimal;

public enum TimeUnit {

    MINUTE {
        @Override
        public String getName() {
            return "分钟";
        }
    },
    HOUR {
        @Override
        public String getName() {
            return "小时";
        }
    },
    DAY {
        @Override
        public String getName() {
            return "天";
        }
    },
    WEEK {
        @Override
        public String getName() {
            return "周";
        }
    },
    MONTH {
        @Override
        public String getName() {
            return "月";
        }
    },
    YEAR {
        @Override
        public String getName() {
            return "年";
        }
    };

    public abstract String getName();

    /**
     * 把相应的时间转为成分钟
     * 
     * @param i
     *            数量
     * @param timeUnit
     *            日期单位
     * @return
     */
    public static int convert2Minutes(int i, TimeUnit timeUnit) {
        if (HOUR == timeUnit) {
            return i * 60;
        }
        if (DAY == timeUnit) {
            return i * minuteOfDay();
        }
        if (WEEK == timeUnit) {
            return i * 7 * minuteOfDay();
        }
        if (MONTH == timeUnit) {
            return i * 30 * minuteOfDay();
        }
        if (YEAR == timeUnit) {
            return i * 365 * minuteOfDay();
        }

        return i;
    }

    /**
     * 把分钟转换为其他的时间单位. 转换顺序为年->月->周->天->小时->分钟, 如果参数能被年整除则返回以年为单位的时间, 如果不能被年
     * 整除, 则看是否能被月整除.... 以此类推. 
     * 该方法与{@link convert2Minutes}想对应
     * 
     * @param minutes
     *            分钟
     * @return
     */
    public static Time minutes2Unit(Integer minutes) {
        if (minutes == null) {
            return null;
        }
        int year = minutes % (365 * minuteOfDay());
        if (year == 0) {
            return new Time(minutes / (365 * minuteOfDay()), YEAR);
        }
        int month = minutes % (30 * minuteOfDay());
        if (month == 0) {
            return new Time(minutes / (30 * minuteOfDay()), MONTH);
        }
        int week = minutes % (7 * minuteOfDay());
        if (week == 0) {
            return new Time(minutes / (7 * minuteOfDay()), WEEK);
        }
        int day = minutes % minuteOfDay();
        if (day == 0) {
            return new Time(minutes / minuteOfDay(), DAY);
        }
        int hour = minutes % 60;
        if (hour == 0) {
            return new Time(minutes / 60, HOUR);
        }

        return new Time(minutes, MINUTE);
    }

    private static int minuteOfDay() {
        return 24 * 60;
    }

    public static class Time {
        public final int      time;
        public final TimeUnit unit;

        public Time(int time, TimeUnit unit) {
            this.time = time;
            this.unit = unit;
        }

        @Override
        public String toString() {
            return "Time [time=" + time + ", unit=" + unit + "]";
        }

    }

    /**
     * 获取相对时间
     * @param seconds 秒
     * @return
     */
    public static String getTimeStr(long seconds) {
        long minute = seconds / 60;
        String timeStr = "";
        int year = divideFloor(minute, convert2Minutes(1, YEAR)).intValue();
        if (year > 0) {
            timeStr += year + YEAR.getName();
            minute %= convert2Minutes(1, YEAR);
        }
        int mouth = divideFloor(minute, convert2Minutes(1, MONTH)).intValue();
        if (mouth > 0) {
            timeStr += mouth + MONTH.getName();
            minute %= convert2Minutes(1, MONTH);
        }
        int day = divideFloor(minute, minuteOfDay()).intValue();
        if (day > 0) {
            if (day == 1) {
                timeStr = "24" + HOUR.getName();
            } else {
                timeStr += day + DAY.getName();
            }
            minute %= minuteOfDay();
        }

        int hour = divideFloor(minute, 60).intValue();
        if (hour > 0) {
            timeStr += hour + HOUR.getName();
            minute %= 60;
        }

        if (minute > 0) {
            timeStr += minute + MINUTE.getName();
        } else if (timeStr == "") {
            timeStr = "0" + MINUTE.getName();
        }

        return timeStr;
    }

    private static BigDecimal divideFloor(double up, double down) {
        return BigDecimal.valueOf(up).divide(BigDecimal.valueOf(down), BigDecimal.ROUND_FLOOR);
    }
}
