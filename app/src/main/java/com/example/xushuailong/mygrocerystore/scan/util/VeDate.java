package com.example.xushuailong.mygrocerystore.scan.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class VeDate {

	/**
	 * Current time
	 * 
	 * @return long yyMMddHHmm
	 */
	public static long getNewestDateTime() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

		String dateString = formatter.format(currentTime);
		String date10 = dateString.substring(2, 12);// yyMMddHHmm
		int len = date10.length();
		long result = 0;
		for (int i = 0; i < len; i++) {
			result = result * 10 + (date10.charAt(i) - '0');
		}
		return result;
	}

	/**
	 * 
	 * @param dateDate
	 * @return long yyMMddHHmm
	 */
	public static long DateTimeTolng(Date datetime) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(datetime);

		String date10 = dateString.substring(2, 12);// yyMMddHHmm
		int len = date10.length();
		long result = 0;
		for (int i = 0; i < len; i++) {
			result = result * 10 + (date10.charAt(i) - '0');
		}
		return result;
	}

	/**
	 * 
	 * @param SecondsExpect 20 or -20
	 * @return The value is the number of milliseconds since Jan. 1, 1970, midnight GMT.
	 */
	public static long getExpectTimefromNow(long SecondsExpect) {
		long Time =  new Date().getTime() + SecondsExpect * 1000;
		return Time;

	}
	
	/**
	 * 
	 * @param SecondsExpect 20 or -20
	 * @return yyyyMMddHHmmss
	 */
	public static String getExpectTimefromNow2(int SecondsExpect) {

		Date curdate = new Date();
		long Time = (curdate.getTime() / 1000) + SecondsExpect;
		curdate.setTime(Time * 1000);
		return getTimeyyyyMMddHHmmss(curdate);
		 

	}
	
	/**
	 * 
	 * @param SecondsExpect 20 or -20
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getExpectTimefromNow3(int SecondsExpect) {

		Date curdate = new Date();
		long Time = (curdate.getTime() / 1000) + SecondsExpect;
		curdate.setTime(Time * 1000);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(curdate);
		return dateString;
		 

	}

	/**
	 * 
	 * @param CurDate
	 *            Date
	 * @param MinutesExpect
	 *            20 or -20
	 * @return if an error occurs, return null; or return long yyMMddHHmm
	 */
	public static long getExpectTime(Date curdate, int MinutesExpect) {

		if (curdate == null)
			return 0;
		long Time = (curdate.getTime() / 1000) + MinutesExpect * 60;
		curdate.setTime(Time * 1000);
		return DateTimeTolng(curdate);

	}

	/**
	 * 
	 * @return long yyMMdd
	 */
	public static long getToday() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

		String dateString = formatter.format(currentTime);
		String date10 = dateString.substring(2, 8);// yyMMdd
		int len = date10.length();
		long result = 0;
		for (int i = 0; i < len; i++) {
			result = result * 10 + (date10.charAt(i) - '0');
		}
		return result;
	}

	/**
	 * milliseconds
	 * @return Returns this Date as a millisecond value. <br>
	 *         The value is the number of milliseconds since Jan. 1, 1970, midnight
	 *         GMT.
	 */
	public static long getCurDateTime() {
		return new Date().getTime();

	}

	/**
	 * 
	 * @return "yyyy-MM-dd", default "2013-06-01"
	 */
	public static String getCurDay() {
		try{
			Date currentTime = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			return formatter.format(currentTime);
		}catch(Exception e) {
			return "2013-06-01";
		} 
	}

	/**
	 * 
	 * @return "HH:mm:ss"
	 */
	public static String getCurTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		Date currentTime = new Date();
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 
	 * @return "yyyy-MM-dd HH:mm:ss"
	 */
	public static String getCurDayTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date currentTime = new Date();
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 
	 * @return "yyyyMMddHHmmss"
	 */
	public static String getCurTimeyyyyMMddHHmmss() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date currentTime = new Date();
		String dateString = formatter.format(currentTime);
		return dateString;
	}
	
	/**
	 * 
	 * @return "yyyy-MM-dd HH:mm:ss"
	 */
	public static String getCommonDateTime(Date dateTime) {
		if(dateTime == null) dateTime = getNow();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(dateTime);
		return dateString;
	}
	
	/**
	 * 
	 * @return "yyyyMMddHHmmss"
	 */
	public static String getTimeyyyyMMddHHmmss(Date dateTime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(dateTime);
		return dateString;
	}
	
	/**
	 * when bigger than 24*3600, will be subtracted 24*3600
	 * @return "HH:mm:ss" 
	 */
	public static String getTimeHHmmss(long seconds) {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		Date currentTime = new Date( (seconds - 8 * 3600) * 1000 );  // from 1970.1.1 8:00:00
		String dateString = formatter.format(currentTime);
		return dateString;
	}
 
	/**
	 * @param seconds >= 0
	 * @return "dd天HH时mm分" 1天01时12分/00时01分  or empty
	 */
	public static String getTimeHHmm(long seconds) {
		if(seconds < 0) return "";
		String hh = "";
		String mm = "";
		String dd = "";
		long tmp = seconds / (3600 * 24);
		if(tmp > 0)
		   dd = "" + tmp + "天";
		
		tmp = (seconds % (3600 * 24)) / 3600;
		
		if(tmp < 10 && tmp >= 0)
			hh = "0" + tmp;
		else
			hh = "" + tmp;
		
		tmp = (seconds % 3600) / 60;
	 
		if(tmp < 10 && tmp >= 0)
			mm = "0" + tmp;
		else
			mm = ""+tmp;
	
		
		return  dd + hh+"时"+mm+"分";
		 
	}
 
	/**
	 * @return "dd天HH时mm分ss秒" 1天01时12分49秒/00时00分09秒
	 */
	public static String getTimeHHmmss3(long seconds) {
		String hh = "";
		String mm = "";
		String ss = "";
		String dd = "";
		long tmp = seconds / (3600 * 24);
		if(tmp > 0)
		   dd = "" + tmp + "天";
		
		tmp = (seconds % (3600 * 24)) / 3600;
		
		if(tmp < 10 && tmp >= 0)
			hh = "0" + tmp;
		else
			hh = "" + tmp;
		
		tmp = (seconds % 3600) / 60;
	 
		if(tmp < 10 && tmp >= 0)
			mm = "0" + tmp;
		else
			mm = ""+tmp;
		
		tmp = seconds % 60;
		
		if(tmp < 10 && tmp >= 0)
			ss = "0" + tmp;
		else
			ss = "" + tmp;
		
		return  dd + hh+"时"+mm+"分"+ss+"秒";
		 
	}
	

	public static boolean isDateTime(String strDateTime) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			ParsePosition pos = new ParsePosition(0);
			Date date = formatter.parse(strDateTime, pos);
			if(date != null)
				return true;			
		} catch (Exception e) {
			 
		}
		return false;
	}
	
	/**
	 * 
	 * @param strDate
	 *            "yyyy-MM-dd HH:mm:ss"
	 * @return if an error occurs, return cur Date;
	 */
	public static Date StrToDateTime(String strDateTime) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			ParsePosition pos = new ParsePosition(0);
			Date strtodate = formatter.parse(strDateTime, pos);
			return strtodate;
		} catch (Exception e) {
			return getNow();
		}
	}

	/**
	 * 
	 * @param strDate
	 *            yyyy-MM-dd
	 * @return if an error occurs, return cur Date;
	 */
	public static Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		ParsePosition pos = new ParsePosition(0);
		try {
			Date strtodate = formatter.parse(strDate, pos);
			return strtodate;
		} catch (Exception e) {
			return getNow();
		}
	}

	/**
	 * 
	 * @param strDate yyyyMMddhhmm
	 * @return if an error occurs, return cur Date;
	 */
	public static Date strToDate2(String Day) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmm");
		ParsePosition pos = new ParsePosition(0);
		try {
			Date strtodate = formatter.parse(Day, pos);
			return strtodate;
		} catch (Exception e) {
			e.printStackTrace();
			return getNow();
		}
	}

	/**
	 * 
	 * @param strDate yyyyMMddHHmmss
	 * @return if an error occurs, return cur Date;
	 */
	public static Date strToDate3(String Day) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		try {
			Date date = formatter.parse(Day);
			return date;
		} catch (Exception e) {
			e.printStackTrace();
			return getNow();
		}
	}
	
	/**
	 * 
	 * @param dateDate
	 * @return long yyMMdd
	 */
	public static long DayTolng(Date datetime) {
		if(datetime == null) 
			datetime = getNow();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(datetime);

		String date10 = dateString.substring(2, 8);// yyMMdd
		int len = date10.length();
		long result = 0;
		for (int i = 0; i < len; i++) {
			result = result * 10 + (date10.charAt(i) - '0');
		}
		return result;
	}

	/**
	 * 
	 * @param date
	 * @return "yyyy-MM-dd"
	 */
	public static String getDay(String date) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = formatter.format(formatter.parse(date));
			return dateString;
		} catch (Exception e) {
			return getCurDay();
		}
	}
	
	/**
	 * 
	 * @param date
	 * @return "yyyy-MM-dd"
	 */
	public static String getDay(Date date) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = formatter.format(date);
			return dateString;
		} catch (Exception e) {
			return getCurDay();
		}
	}

	/**
	 * 
	 * @param date
	 *            The value is the number of seconds since Jan. 1, 1970,
	 *            midnight GMT.
	 * @return "yyyy-MM-dd"
	 */
	public static String getDay(long date) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = formatter.format(new Date(date));
			return dateString;
		} catch (Exception e) {
			return getCurDay();
		}
	}

	/**
	 * 
	 * @param date
	 * @return int MMdd
	 */
	public static int getMMdd(Date date) {
		if(date == null) 
			date = getNow();
		SimpleDateFormat formatter = new SimpleDateFormat("MMdd");
		String dateString = formatter.format(date);

		return Integer.parseInt(dateString);

	}
	
	/**
	 * 
	 * @param date
	 * @return  MM-dd
	 */
	public static String getMMdd(String date) {
		try{ 
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = formatter.format(formatter.parse(date));
			return  dateString.substring(5, 10); //MM-dd
		} catch (Exception e) {
			return "01-01";
		}
  
	}

	
	/**
	 * 
	 * @param date
	 * @return  HH:mm
	 */
	public static String getHHmm(String date) {
		try{ 
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String dateString = formatter.format(formatter.parse(date));
			return  dateString.substring(11, 16); //HH:mm
		} catch (Exception e) {
			return "00:00";
		}
  
	}
	
	
	/**
	 * 
	 * @param date
	 * @return  MM-dd HH:mm
	 */
	public static String getMMddHHmm(String date) {
		try{ 
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String dateString = formatter.format(formatter.parse(date));
			return  dateString.substring(5, 16); //MM-dd HH:mm
		} catch (Exception e) {
			return "01-01 00:00";
		}
  
	}

	/**
	 * 
	 * @param date
	 * @return yyyy-MM-dd HH:mm
	 */
	public static String getYYMMDDHHMM(Date date) {
		if(date == null) 
			date = getNow();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String dateString = formatter.format(date);
		return dateString;
	}
	
	/**
	 * 
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getYYMMDDHHMMSS(Date date) {
		if(date == null) 
			date = getNow();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(date);
		return dateString;
	}
	/**
	 * 
	 * @param date long Initializes this Date instance using the specified millisecond value. The value is the number of milliseconds since Jan. 1, 1970 GMT.
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getYYMMDDHHMMSS(long date) {
		return getYYMMDDHHMMSS(new Date(date));
	}

	public static Date getNow() {
		Date currentTime = new Date();
		return currentTime;
	}

	/**
	 * get the time point elapsed from now by day
	 * 
	 * @param dayElapse
	 *            100 or -100
	 * @return
	 */
	public static Date getExpectDateByDays(long dayElapse) {
		Date date = new Date();
		long date_3_hm = date.getTime() - 3600000 * 24 * dayElapse;
		return new Date(date_3_hm);

	}

	/**
	 * get the time point elapsed from now by seconds
	 * 
	 * @param secondsElapse
	 *            100 or -100
	 * @return
	 */
	public static Date getExpectDateBySeconds(long secondsElapse) {
		Date date = new Date();
		long date_s = date.getTime() - 1000 * secondsElapse;
		return new Date(date_s);

	}

	/**
	 * get the time point elapsed from now by seconds
	 * 
	 * @param secondsElapse
	 *            100 or -100
	 * @return
	 */
	public static Date getExpectDateBySeconds(Date now, long secondsElapse) {

		long date_s = now.getTime() - 1000 * secondsElapse;
		return new Date(date_s);

	}

	public static int getElapseTimeBySeconds(Date oldDate) {
		long oldertime = oldDate.getTime();
		long now = new Date().getTime();
		return (int) (now - oldertime) / 1000;
	}

	public static int getElapseTimeBySeconds(Date now, Date oldDate) {
		long oldertime = oldDate.getTime();
		return (int) (now.getTime() - oldertime) / 1000;
	}

	public static String getElapseTimeCN(Date oldDate) {
		if (oldDate == null)
			return "刚刚";
		long oldertime = oldDate.getTime();
		long now = new Date().getTime();
		long secondsElapse = (now - oldertime) / 1000;
		if (secondsElapse <= 0)
			return "刚刚";
		if (secondsElapse <= 60)
			return "" + secondsElapse + "秒钟前";
		long hours = secondsElapse / 3600;
		long minutes = (secondsElapse - hours * 3600) / 60;
		if (hours <= 0)
			return "" + minutes + "分钟前";
		if (hours <= 23 && hours > 0)
			return "" + hours + "小时前";

		int days = (int) (hours / 24);
		if (days > 0 && days <= 30)
			return "" + days + "天前";
//		long months = hours / (24 * 30);
//		if (months > 0 && months <= 11)
//			return "" + months + "月前";
//		long years = hours / (24 * 365);
//		return "" + years + "年前";
		return "30天前";
	}

	public static String getCurHour() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		String hour;
		hour = dateString.substring(11, 13);
		return hour;
	}

	public static String getCurMinute() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		String min;
		min = dateString.substring(14, 16);
		return min;
	}

	/**
	 * 
	 * @param customDateFormat such as "yyyy-MM-dd"
	 * @return if an error occurs, return null;
	 */
	public static String getCustomDate(String customDateFormat) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(customDateFormat);
		String dateString = formatter.format(currentTime);
		return dateString;
	}
 

	/**
	 * day = NewDate - OldDate
	 * 
	 * @param NewDate yyyy-MM-dd
	 * @param OldDate yyyy-MM-dd
	 * @return if an error occurs, return -1, same day return 1;
	 */
	public static long DaysBetween(String NewDate, String OldDate) {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
		long day = 0;
		try {
			Date newdate = myFormatter.parse(NewDate);
			Date olddate = myFormatter.parse(OldDate);
			day = (newdate.getTime() - olddate.getTime())
					/ (24 * 60 * 60 * 1000) + 1;
		} catch (Exception e) {
			return -1;
		}
		return day;
	}
	
	/**
	 * times = NewDate - OldDate
	 * 
	 * @param NewDate yyyy-MM-dd hh:mm:ss
	 * @param OldDate yyyy-MM-dd  hh:mm:ss
	 * @return  hours; if an error occurs, return 0;
	 */
	public static long HoursBetween(String NewDate, String OldDate) {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long times = 0;
		try {
			Date newdate = myFormatter.parse(NewDate);
			Date olddate = myFormatter.parse(OldDate);
			times = (newdate.getTime() - olddate.getTime()) / (1000 * 60 * 60);
		} catch (Exception e) {
		}
		return times;
	}
	
	/**
	 * times = NewDate - OldDate
	 * 
	 * @param NewDate yyyy-MM-dd hh:mm:ss
	 * @param OldDate yyyy-MM-dd  hh:mm:ss
	 * @return  milliseconds; if an error occurs, return 0;
	 */
	public static long TimesBetween(String NewDate, String OldDate) {
		SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long times = 0;
		try {
			Date newdate = myFormatter.parse(NewDate);
			Date olddate = myFormatter.parse(OldDate);
			times = (newdate.getTime() - olddate.getTime());
		} catch (Exception e) {
		}
		return times;
	}

	/**
	 * times = date - now
	 *  
	 * @param date  
	 * @return  seconds; 
	 */
	public static long TimesBetween(Date date) {
		long times = 0;
		try {
			Date newdate = new Date();
			times = (date.getTime() - newdate.getTime()) / 1000;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return times;
	}
	
	public static boolean isSameDay(Date newdate, Date olddate) {
		if (newdate == null || olddate == null)
			return false;
		long day = DaysBetween(VeDate.getDay(newdate), VeDate.getDay(olddate));
		if (day == 1)
			return true;
		return false;
	}

	/**
	 * day = ExpectDate - today + 1
	 * 
	 * @param ExpectDate yyyy-MM-dd
	 * @return if an error occurs, return -1, same day return 1;
	 */
	public static long DaysRemain(String ExpectDate) {
		if(ExpectDate == null) return -1;
		return DaysBetween(ExpectDate, getCurDay());
	}

	/**
	 * 
	 * @param Curdate yyyy-MM-dd
	 * @param DaysExpect 120 or -120
	 * @return if an error occurs, return null;
	 */
	public static String getExpectDay(String Curdate, String DaysExpect) {
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String mdate = "";
			Date curdate = strToDate(Curdate);
			if (curdate == null)
				return null;
			long myTime = (curdate.getTime() / 1000)
					+ Integer.parseInt(DaysExpect) * 24 * 60 * 60;
			curdate.setTime(myTime * 1000);
			mdate = format.format(curdate);
			return mdate;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * 
	 * @param date
	 *            yyyy-MM-dd
	 * @return
	 */
	public static boolean isLeapYear(String date) {

		Date d = strToDate(date);
		if (d == null)
			return false;
		GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
		gc.setTime(d);
		int year = gc.get(Calendar.YEAR);
		if ((year % 400) == 0)
			return true;
		else if ((year % 4) == 0) {
			if ((year % 100) == 0)
				return false;
			else
				return true;
		} else
			return false;
	}

	/**
	 * 
	 * @param YearAndMonth
	 *            yyyy-MM
	 * @return if an error occurs, return null;
	 */
	public static String getEndDateOfMonth(String YearAndMonth) {// yyyy-MM
		if (YearAndMonth.length() < 8)
			return null;
		String EndDateOfMonth = YearAndMonth.substring(0, 7);
		String month = YearAndMonth.substring(5, 7);
		EndDateOfMonth += "-";
		int mon = Integer.parseInt(month);
		if (mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8
				|| mon == 10 || mon == 12) {
			EndDateOfMonth += "31";
		} else if (mon == 4 || mon == 6 || mon == 9 || mon == 11) {
			EndDateOfMonth += "30";
		} else {
			if (isLeapYear(EndDateOfMonth + "01")) {
				EndDateOfMonth += "29";
			} else {
				EndDateOfMonth += "28";
			}
		}
		return EndDateOfMonth;
	}

	public static boolean isSameWeekDates(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
		if (0 == subYear) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {

			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR))
				return true;
		} else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
			if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
					.get(Calendar.WEEK_OF_YEAR))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param DeadLine  yyyy-MM-dd
	 * @return
	 */
	public static boolean isOutDate(String DeadLine) {
		long today = getToday();
		long day = DayTolng(strToDate(DeadLine));
		if (day < today)
			return true;
		return false;
	}

	/**
	 * 
	 * @param StartDay
	 *            yyyymmddhhmm be included
	 * @param EndDay
	 *            yyyymmddhhmm , be included
	 * @return -1, 0(between), 1
	 */
	public static int compareDate(String StartDay, String EndDay) {
		try{
			long today = getCurDateTime();
			long startday = strToDate2(StartDay).getTime();
			long endday =  strToDate2(EndDay).getTime();
	 		if (today < startday)
				return -1;
			else if (today >= startday && today <= endday)
				return 0;
			else
				return 1;
		} catch (Exception e) {
			return -1;
		}
	}
 
	public static String getWeekOfYear() { // ???
		Calendar c = Calendar.getInstance(Locale.CHINA);
		String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
		if (week.length() == 1)
			week = "0" + week;
		String year = Integer.toString(c.get(Calendar.YEAR));
		return year + week;
	}

	/**
	 * 
	 * @param sdate
	 *            yyyy-MM-dd
	 * @param num
	 * @return if an error occurs, return null;
	 */
	public static String getWeek(String sdate, String num) {

		Date date = VeDate.strToDate(sdate);
		if (date == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if (num.equals("1")) // Monday
			c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		else if (num.equals("2")) // Tuesday
			c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		else if (num.equals("3")) // Wednesday
			c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		else if (num.equals("4")) // Thursday
			c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		else if (num.equals("5")) // Friday
			c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		else if (num.equals("6")) // Saturday
			c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		else if (num.equals("0")) // Sunday
			c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
	}

	/**
	 * 
	 * @param sdate
	 *            yyyy-MM-dd
	 * @return if an error occurs, return null;
	 */
	public static String getWeek(String sdate) {

		Date date = VeDate.strToDate(sdate);
		if (date == null)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		return new SimpleDateFormat("EEEE").format(c.getTime());
	}

	/**
	 * 
	 * @param sdate
	 *            yyyy-MM-dd
	 * @return if an error occurs, return null;
	 */
	public static String getWeekStrCN(String sdate) {
		String str = null;
		str = VeDate.getWeek(sdate);
		if ("1".equals(str)) {
			str = "星期一";
		} else if ("2".equals(str)) {
			str = "星期二";
		} else if ("3".equals(str)) {
			str = "星期三";
		} else if ("4".equals(str)) {
			str = "星期四";
		} else if ("5".equals(str)) {
			str = "星期五";
		} else if ("6".equals(str)) {
			str = "星期六";
		} else if ("7".equals(str)) {
			str = "星期日";
		}
		return str;
	}

	public static void main(String[] args) throws Exception {
		try {

			long datetime10 = VeDate.getNewestDateTime();
			System.out.print("datetime10 = " + datetime10);
			System.out.print("string CurDateTime =" + getCurDateTime());

		} catch (Exception e) {
			throw new Exception();
		}
		// System.out.println("sss");
	}
	
	/**
	 * 从UTC时间返回本地时间
	 * @param utcTime 如：2014-02-24T05:55:37.883Z
	 * @return local time
	 */
	public static Date getDateFromUTC(String utcTime) {
		SimpleDateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		if (utcTime == null) {
            return null;
        }
        Date date = null;
        try {
            Calendar cal = Calendar.getInstance();
            // Convert the UTC time to local time.
            cal.setTime(new Date(utcFormat.parse(utcTime).getTime() +
                    cal.getTimeZone().getOffset(cal.getTimeInMillis())));
            date = cal.getTime();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return date;
	}
	//--------for qrcode-----------------------------------------------//
	/**
	 * 
	 * @param strDate yyyyMMddHHmmss
	 * @return if an error occurs, return cur Date;
	 */
	public static Date StrToDateTime2(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		ParsePosition pos = new ParsePosition(0);
		try{
			Date strtodate = formatter.parse(strDate, pos);
			return strtodate;
		}catch(Exception e){
			return getNow();
		}
	}
	/**
	 * 
	 * @param date
	 * @return "MM/dd"
	 */
	public static String getDayNoYear(Date date) {
		try{
			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");
			String dateString = formatter.format(date);
			return dateString;
		}catch(Exception e){
			return getCurDay();
		}
	}
	/**
	 * 
	 * @param dateDate
	 * @return  long yyyyMMdd
	 */
	public static long DayTolong2(Date datetime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String dateString = formatter.format(datetime);
		
		String date10 = dateString.substring(0, 8);//yyyyMMdd
        int len = date10.length();
        long result = 0;
		for(int i = 0; i < len; i++){
		   result = result*10 + (date10.charAt(i)-'0');
		}
		return result;
	}
	/**
	 * 
	 * @param date
	 * @return MM/dd or HH:mm
	 */
	public static String getMinuteTime(Date date){
		boolean isSameDay = DayTolong2(getNow()) == DayTolong2(date);
		if(isSameDay){ 
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
			String dateString = formatter.format(date);
			return dateString;
		}else{
			return getDayNoYear(date);
		}		 
	}
	//---------------------------------qr code -------------------------------------------//
}
