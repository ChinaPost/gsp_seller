package com.framework.utils;

import java.text.DecimalFormat;

/**
 * 数字操作工具类，数字类型格式化
 * 
 * @version tang
 * @version 2016-04-18
 */
public class NumberUtil {
	public static final String doubleFormat_00 = "0.00";// 两位

	/**
	 * 保留几位小数，返回字符串
	 * 
	 * @param data
	 * @param doubleFormat
	 * @return
	 */
	public static String formatNumber(double data, String doubleFormat) {
		DecimalFormat df = new DecimalFormat(doubleFormat);
		return df.format(data);
	}

	/**
	 * 保留几位小数，返回double
	 * 
	 * @param data
	 * @param doubleFormat
	 * @return
	 */
	public static double formatNumberReturnDouble(double data, String doubleFormat) {
		DecimalFormat df = new DecimalFormat(doubleFormat);
		return UtilTools.parseDoubleWhenNullRetur0(df.format(data));
	}

	public static void main(String[] args) {
		System.out.println(NumberUtil.formatNumber(0.123456, NumberUtil.doubleFormat_00));
		System.out.println(NumberUtil.formatNumberReturnDouble(0.123456, NumberUtil.doubleFormat_00));
	}
}