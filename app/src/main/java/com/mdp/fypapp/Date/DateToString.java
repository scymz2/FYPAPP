package com.mdp.fypapp.Date;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;




public class DateToString {


    private static String[] dateFormat = { "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss", "yyyy年MM月dd日HH时mm分ss秒",
            "yyyy-MM-dd", "yyyy/MM/dd", "yy-MM-dd", "yy/MM/dd", "yyyy年MM月dd日", "HH:mm:ss", "yyyyMMddHHmmss", "yyyyMMdd",
            "yyyy.MM.dd", "yy.MM.dd", "MM月dd日" };


    public static Timestamp convUtilCalendarToSqlTimestamp(Calendar date) {
        if (date == null) {
            return null;
        }
        return new Timestamp(date.getTimeInMillis());
    }


    public static Calendar convSqlTimestampToUtilCalendar(Timestamp date) {
        if (date == null) {
            return null;
        }
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(date.getTime());
        return gc;
    }


    public static Calendar parseDate(String dateStr) {
        if ((dateStr == null) || (dateStr.trim().length() == 0)) {
            return null;
        }
        Date result = parseDate(dateStr, 0);
        Calendar cal = Calendar.getInstance();
        cal.setTime(result);
        return cal;
    }


    public static Date parseStringToDate(String dateStr) {
        if ((dateStr == null) || (dateStr.trim().length() == 0)) {
            return null;
        }
        Date result = parseDate(dateStr, 0);
        return result;
    }


    public static Date parseDate(String dateStr, int index) {
        DateFormat df = null;
        try {
            df = new SimpleDateFormat(dateFormat[index]);
            return df.parse(dateStr);
        } catch (ParseException pe) {
            return parseDate(dateStr, index + 1);
        } catch (ArrayIndexOutOfBoundsException aioe) {
        }
        return null;
    }


    public static String toDateTimeStr(Calendar date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(dateFormat[0]).format(date.getTime());
    }


    public static String toDateTimeStr(int formatIndex, Calendar date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(dateFormat[formatIndex]).format(date.getTime());
    }


    public static String toDateStr(Calendar date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(dateFormat[3]).format(date.getTime());
    }


    public static String toDateStrByFormatIndex(Calendar date, int formatIndex) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(dateFormat[formatIndex]).format(date.getTime());
    }


    public static int calendarMinus(Calendar d1, Calendar d2) {
        if ((d1 == null) || (d2 == null)) {
            return 0;
        }
        d1.set(11, 0);
        d1.set(12, 0);
        d1.set(13, 0);
        d2.set(11, 0);
        d2.set(12, 0);
        d2.set(13, 0);
        long t1 = d1.getTimeInMillis();
        long t2 = d2.getTimeInMillis();
        System.out.println("DateUtils: d1 = " + toDateTimeStr(d1) + "(" + t1 + ")");
        System.out.println("DateUtils: d2 = " + toDateTimeStr(d2) + "(" + t2 + ")");
        long daylong = 86400000L;
        t1 -= t1 % daylong;
        t2 -= t2 % daylong;
        long t = t1 - t2;
        int value = (int) (t / daylong);
        System.out.println("DateUtils: d2 -d1 = " + value + " （天）");
        return value;
    }


    public static long calendarminus(Calendar d1, Calendar d2) {
        if ((d1 == null) || (d2 == null)) {
            return 0L;
        }
        return (d1.getTimeInMillis() - d2.getTimeInMillis()) / 86400000L;
    }


    public static Date StringToDate(String dateStr) {
        if ((dateStr == null) || (dateStr.trim().length() == 0)) {
            return null;
        }
        return parseDate(dateStr, 3);
    }


    public static String dateToString(Date date, int index) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(dateFormat[index]).format(date);
    }


    public static String dateToString(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(dateFormat[3]).format(date);
    }


    public static String dateToStringWeek(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(dateFormat[3]).format(date);
    }


    public static Timestamp convUtilDateToSqlTimestamp(Date date) {
        if (date == null) {
            return null;
        }
        return new Timestamp(date.getTime());
    }


    public static Calendar convUtilDateToUtilCalendar(Date date) {
        if (date == null) {
            return null;
        }
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(date.getTime());
        return gc;
    }


    public static Timestamp parseTimestamp(String dateStr, int index) {
        DateFormat df = null;
        try {
            df = new SimpleDateFormat(dateFormat[index]);
            return new Timestamp(df.parse(dateStr).getTime());
        } catch (ParseException pe) {
            return new Timestamp(parseDate(dateStr, index + 1).getTime());
        } catch (ArrayIndexOutOfBoundsException aioe) {
        }
        return null;
    }


    public static Timestamp parseTimestamp(String dateStr) {
        DateFormat df = null;
        try {
            df = new SimpleDateFormat(dateFormat[0]);
            return new Timestamp(df.parse(dateStr).getTime());
        } catch (ParseException pe) {
            return null;
        } catch (ArrayIndexOutOfBoundsException aioe) {
        }
        return null;
    }


    public static Timestamp parseTimestamp(Calendar calendar) {
        return new Timestamp(calendar.getTimeInMillis());
    }


    public static int calcMonthDays(Calendar date) {
        Calendar t1 = (Calendar) date.clone();
        Calendar t2 = (Calendar) date.clone();
        int year = date.get(1);
        int month = date.get(2);
        t1.set(year, month, 1);
        t2.set(year, month + 1, 1);
        t2.add(6, -1);
        return calendarMinus(t2, t1) + 1;
    }


    public static String getWeekOfDate(String str, String sign) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] weekDaysName = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        String[] weekDaysCode = { "0", "1", "2", "3", "4", "5", "6" };
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(7) - 1;
        if ("cn".equals(sign)) {
            return weekDaysName[intWeek];
        }
        return weekDaysCode[intWeek];
    }


    public static String addDay(String str, int amount) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        cal1.add(5, amount);
        return format.format(cal1.getTime());
    }


    public static Date parseDate(String sDate, String sFormat) {
        SimpleDateFormat formatter = null;
        Date utildate = null;
        formatter = new SimpleDateFormat(sFormat);
        try {
            utildate = formatter.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return utildate;
    }


    public static float countSc(Calendar jssj, Calendar kssj) {
        long sc = 0L;
        long s1 = jssj.getTimeInMillis();
        long s2 = kssj.getTimeInMillis();
        sc = s1 - s2;
        float result = (float) (sc / 1000L);
        return result;
    }


    public static long countSc(Date jssj, Date kssj) {
        long sc = 0L;
        sc = jssj.getTime() - kssj.getTime();
        long result = sc / 1000L;
        return result;
    }


    public static XMLGregorianCalendar dateToXmlDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        DatatypeFactory dtf = null;
        try {
            dtf = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException localDatatypeConfigurationException) {
        }
        XMLGregorianCalendar dateType = dtf.newXMLGregorianCalendar();
        dateType.setYear(cal.get(1));


        dateType.setMonth(cal.get(2) + 1);
        dateType.setDay(cal.get(5));
        dateType.setHour(cal.get(11));
        dateType.setMinute(cal.get(12));
        dateType.setSecond(cal.get(13));
        return dateType;
    }


    public static boolean isValidDateStr(String dateStr, String str) {
        boolean convertSuccess = true;


        SimpleDateFormat format = new SimpleDateFormat(dateStr);
        try {
            format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            convertSuccess = false;
        }
        return convertSuccess;
    }


    public static long date2Date(String sdate1, String sdate2) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse(sdate1);
        Date date2 = sdf.parse(sdate2);
        return date2.getTime() / 86400000L - date1.getTime() / 86400000L;
    }


    public static String addDay(String str, int amount, int index) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(dateFormat[index]);
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);
        cal1.add(5, amount);
        return format.format(cal1.getTime());
    }


    public static String dateToStrForWs(Date date) {
        if (date == null) {
            return "";
        }
        return dateToString(date, 9);
    }

}