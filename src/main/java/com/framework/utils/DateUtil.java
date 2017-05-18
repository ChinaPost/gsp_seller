package com.framework.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author tang E-mail: killerover84@163.com
 * @version 2016年5月16日 下午4:17:16 类说明
 */
public class DateUtil {

	/** 按 DateFormat 格式化Date 输出格式化后的字符串 */
	public static String getDateStr(Date date, DateFormat dateFormate) {
		return dateFormate.format(date);
	}

	/**
	 * 返回当前时间
	 * 
	 * @param fmtString（格式）
	 * @return
	 */
	public static String getDateStr(String fmtString) {
		if (fmtString == null)
			fmtString = "yyyyMMdd";
		SimpleDateFormat dateFormate = new SimpleDateFormat(fmtString);
		return dateFormate.format(new Date());
	}

	/**
	 * 取得当前时间yyyyMMdd
	 * 
	 * @return
	 */
	public static String getTodayStr() {
		return getDateStr(new Date(), DATEFORMAT_YYYYMMDD);
	}

	/**
	 * 取得当前时间yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getToday() {
		return getDateStr(new Date(), DATEFORMAT_YYYY_MM_DD);
	}

	/**
	 * 取得当前时间yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getTodayTime() {
		return getDateStr(new Date(), DATEFORMAT_YYYY_MM_DD_HH_MM_SS);
	}

	/**
	 * 获取24小时时间列表(后缀可选择),格式：HH:SS
	 * 
	 * @param isEnd
	 *            true [00:59], false [00:00]
	 * @return <code>[00:00],[01:00] ... [23:00] OR [00:59] ... [23:59]</code>
	 */
	public static List<String> get24Hhss(boolean isEnd) {
		String endStr = isEnd ? ":59" : ":00";
		List<String> timeList = new ArrayList<String>(24);
		for (int i = 0; i <= 9; i++) {
			timeList.add("0" + i + endStr);
		}
		for (int i = 10; i <= 23; i++) {
			timeList.add(i + endStr);
		}
		return timeList;
	}

	/**
	 * 获取小时字符串 如：00:00 delimiter 02:59
	 * 
	 * @param interval
	 * @param delimiter
	 * @return
	 */
	public static List<String> get24Hhss(int interval, String delimiter) {
		if (interval > 24 || 24 % interval != 0 || null == delimiter) {
			return null;
		}
		int step = 24 / interval;
		List<String> timeList = new ArrayList<String>(step);
		for (int i = 0; i < step; i++) {
			String prefix = "";
			String suffix = "";
			int temp = i * interval;

			if (temp <= 9) {
				prefix = "0" + temp;
			} else {
				prefix = temp + "";
			}
			if ((temp + interval) <= 10) {
				suffix = "0" + (temp + interval - 1);
			} else {
				suffix = temp + interval - 1 + "";
			}
			timeList.add(prefix + ":00" + delimiter + suffix + ":59");
		}
		return timeList;
	}

	/**
	 * 格式化短日期字符串 yyyy-mm-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String getFmtShortDateStr(String date) {
		if (StringUtils.isNotEmpty(date) && date.length() >= 8) {
			String str = "-"; // 2013 - 01 - 08
			return new StringBuilder(date.substring(0, 4)).append(str).append(date.substring(4, 6)).append(str)
					.append(date.substring(6, 8)).toString();
		}
		return date;
	}

	/**
	 * 格式化长日期字符串 yyyy-mm-dd hh24:mi:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String getFmtLongDateStr(String date) {
		if (StringUtils.isNotEmpty(date) && date.length() >= 14) {
			String str = ":"; // 2013 - 01 - 08 08 : 50 : 00
			return new StringBuilder(getFmtShortDateStr(date)).append(" ").append(date.substring(8, 10)).append(str)
					.append(date.substring(10, 12)).append(str).append(date.substring(12)).toString();
		}
		return date;
	}

	/**
	 * 获取指定日期所在月的第一天零点 Date
	 */
	public static Date getMonthStartDay(Date date) {
		return new DateTime(date).dayOfMonth().withMinimumValue().withTimeAtStartOfDay().toDate();
	}

	/**
	 * 获取指定日期所在月的最后一天零点 Date
	 */
	public static Date getMonthLastDay(Date date) {
		return new DateTime(date).dayOfMonth().withMaximumValue().withTimeAtStartOfDay().toDate();
	}

	/**
	 * 返回指定字符串的日期
	 * 
	 * @param str
	 * @param dateFormat
	 *            参考格式： <code>
	 *  <p>yyyy/MM/dd
	 *  <p>yyyy-MM-dd HH:mm:ss
	 * 	<p>yyyy/MM/dd hh:mm:ss.SSSa
	 * 	<p>yyyy/MM/dd HH:mm ZZZZ
	 *  <p>yyyy/MM/dd HH:mm Z
	 * </code>
	 * @return java.util.Date
	 */
	public static Date getDateFromStr(String str, String dateFormat) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormat);
		return formatter.parseDateTime(str).toDate();
	}

	/**
	 * @param startDateStr
	 *            开始日期字符串
	 * @param endDateStr
	 *            结束日期字符串
	 * @param periodType
	 *            区间的类型， 如按月：PeriodType.months()
	 * @return Period
	 */
	public static Period getPeriod(String startDateStr, String endDateStr, String dateFormat, PeriodType periodType) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormat);
		return new Period(formatter.parseDateTime(startDateStr), formatter.parseDateTime(endDateStr), periodType);
	}

	public static Period getPeriod(Date startDate, Date endDate, PeriodType periodType) {
		return new Period(new DateTime(startDate), new DateTime(endDate), periodType);
	}

	/**
	 * 判断 src 是否在 startDateStr 至 endDateStr之间
	 * 
	 * @param src
	 *            目标测试日期
	 * @param startDateStr
	 *            开始日期
	 * @param endDateStr
	 *            结束日期
	 * @param dateFormat
	 * @return <code> (startDateStr <= src < endDateStr) & (src != endDateStr) </code>
	 */
	public static boolean between(String src, String startDateStr, String endDateStr, String dateFormat) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern(dateFormat);
		Interval interval = new Interval(formatter.parseDateTime(startDateStr), formatter.parseDateTime(endDateStr));
		return interval.contains(formatter.parseDateTime(src));
	}

	/** 获取起止日期之间的月份数量 */
	public static int getMonthPeriod(String startDateStr, String endDateStr, String dateFormat) {
		return getPeriod(startDateStr, endDateStr, dateFormat, PeriodType.months()).getMonths();
	}

	/** 获取起止日期之间的月份数量 */
	public static int getMonthPeriod(Date startDate, Date endDate) {
		return getPeriod(startDate, endDate, PeriodType.months()).getMonths();
	}

	/** 添加月份 */
	public static Date plusMonths(Date date, int i) {
		return new DateTime(date).plusMonths(i).toDate();
	}

	/**
	 * 按指定类型获取日期中的字段值
	 *
	 * @param type
	 * 
	 *            <pre>
	 * y  年
	 * M  年中的月份
	 * d  月份中的天数
	 * F  月份中的星期
	 * H  一天中的小时数
	 * m  小时中的分钟数
	 * others -1
	 *            </pre>
	 * 
	 * @return
	 */
	public static int getDateField(DateTime dateTime, char type) {

		int value = -1;

		switch (type) {
		case 'y':
			value = dateTime.getYear();
			break;

		case 'M':
			value = dateTime.getMonthOfYear();
			break;

		case 'd':
			value = dateTime.getDayOfMonth();
			break;

		case 'F':
			value = dateTime.getDayOfWeek();
			break;

		case 'H':
			value = dateTime.getHourOfDay();
			break;

		case 'm':
			value = dateTime.getMinuteOfHour();
			break;

		default:
			value = -1;
		}

		return value;
	}

	/**
	 * 按指定类型获取当前 日期中的字段值
	 *
	 * @param type
	 *            {@link DateUtils#getDateField(DateTime, char)}
	 * @return int
	 */
	public static int getDateField(char type) {
		return getDateField(new DateTime(), type);
	}

	/**
	 * 获取当前日期的 yyyy MM dd 00:00:00 0000 即当日的开始
	 * 
	 * @return date
	 */
	public static Date truncSysdate() {
		return new DateTime().withTimeAtStartOfDay().toDate();
	}

	/** yyyyMMdd */
	public static final SimpleDateFormat DATEFORMAT_YYYYMMDD = new SimpleDateFormat("yyyyMMdd");
	/** yyyyMMddHHmmss */
	public static final SimpleDateFormat DATEFORMAT_YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");
	/** yyyy-MM-dd */
	public static final SimpleDateFormat DATEFORMAT_YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
	/** yyyy-MM-dd HH:mm:ss */
	public static final SimpleDateFormat DATEFORMAT_YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 时间格式字符串转成时间类型
	 * 
	 * @param date
	 * @param dateFormate
	 * @return
	 */
	public static Date getDateByStr(String date, DateFormat dateFormate) {
		Date rdDate = null;
		try {
			rdDate = dateFormate.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("时间转化错误");
		}

		return rdDate;
	}

	/**
	 * 取得年月（yyyyMM）
	 * 
	 * @return
	 */
	public static String getYearMonth() {
		Calendar calendar = Calendar.getInstance(); // 得到日历
		String ym = new StringBuffer().append(calendar.get(Calendar.YEAR)).append(calendar.get(Calendar.MONTH) + 1)
				.toString();
		return ym;
	}

	/**
	 * 返回差距时间 (天单位)
	 * 
	 * @param date
	 * @return
	 */
	public static String disDate(String date, int index, DateFormat dateFormate) {

		Date d = new Date();
		if (null != date && !date.isEmpty()) {
			d = DateUtil.getDateByStr(date, dateFormate);
		}
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(d);// 把当前时间赋给日历
		calendar.add(Calendar.DAY_OF_MONTH, index); // 设置为前一天
		Date dBefore = calendar.getTime(); // 得到前一天的时间

		return DateUtil.getDateStr(dBefore, dateFormate); // 格式化前一天
	}
	
	/**
	 * 计算两个时间相差几个小时
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int hoursBetween2(Date startTime, Date endTime) {
		long between_days = (endTime.getTime() - startTime.getTime()) / (1000 * 3600);
		return Integer.parseInt(String.valueOf(between_days));  
	}

}
