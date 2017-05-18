package com.framework.utils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import sun.misc.BASE64Decoder;

/**
 * 字符串操作工具类
 * 
 * @version tang
 * @version 2016-04-18
 */
public class StringUtil {
	/**
	 * 去掉字符串的开头与结尾的空白
	 */
	public static String trim(String str) {
		return StringUtils.stripToEmpty(str);
	}

	/**
	 * 去掉字符串数组中每个字符元素的开头与结尾的空白
	 */
	public static String[] trim(String[] strs) {
		if (isEmptyArray(strs)) {
			return strs;
		}
		for (int i = 0, len = strs.length; i < len; i++) {
			strs[i] = trim(strs[i]);
		}
		return strs;
	}

	/**
	 * 判断字符串是否为空值或者长度为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {

		return str == null || str.trim().length() == 0;
	}

	/**
	 * 判断字符串是否不为空(包括不为空和不为空字符串)
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 判断字符串是否可以转为数值
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNummeric(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0)
			return false;
		for (int i = 0; i < strLen; i++)
			if (!Character.isDigit(str.charAt(i)))
				return false;
		return true;
	}

	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0)
			return true;
		for (int i = 0; i < strLen; i++)
			if (!Character.isWhitespace(str.charAt(i)))
				return false;

		return true;
	}

	/**
	 * 接受一个object参数
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(Object str) {
		if (null == str)
			return true;

		return isBlank(str.toString());
	}

	/**
	 * 判断两个String类型数据是否不相等 如果两个字符串的值相同，则返回false 如果两个字符串的值不同，则返回true
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean isChange(String str1, String str2) {
		if (isNotEmpty(str1) && isNotEmpty(str2) && str1.equals(str2)) {
			return false;
		} else if (isEmpty(str1) && isEmpty(str2)) {
			return false;
		}
		return true;
	}

	/**
	 * 判断两个String类型数据是否相等 如果两个字符串的值相同，则返回true (如果两个字符串都为 空，也返回true )
	 * 如果两个字符串的值不同，则返回false
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean isEquls(String str1, String str2) {
		if (isNotEmpty(str1) && isNotEmpty(str2) && str1.equals(str2)) {
			return true;
		} else if (isEmpty(str1) && isEmpty(str2)) {
			return true;
		} else
			return false;
	}

	/**
	 * 判断两个Date类型数据是否一样
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean isChange(Date str1, Date str2) {
		if (null != str1 && null != str2 && str1.equals(str2)) {
			return false;
		} else if (null == str1 && null == str2) {
			return false;
		}
		return true;
	}

	/**
	 * 判断两个Long类型数据是否一样
	 * 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean isChange(Long str1, Long str2) {
		if (null != str1 && null != str2 && str1.equals(str2)) {
			return false;
		} else if (null == str1 && null == str2) {
			return false;
		}
		return true;
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static String trimLeadingWhitespace(String str) {
		if (isEmpty(str))
			return str;
		StringBuffer buf;
		for (buf = new StringBuffer(str); buf.length() > 0 && Character.isWhitespace(buf.charAt(0)); buf
				.deleteCharAt(0))
			;
		return buf.toString();
	}

	public static String trimTrailingWhitespace(String str) {
		if (isEmpty(str))
			return str;
		StringBuffer buf;
		for (buf = new StringBuffer(str); buf.length() > 0 && Character.isWhitespace(buf.charAt(buf.length() - 1)); buf
				.deleteCharAt(buf.length() - 1))
			;
		return buf.toString();
	}

	public static String trimLeadingCharacter(String str, char leadingCharacter) {
		if (isEmpty(str))
			return str;
		StringBuffer buf;
		for (buf = new StringBuffer(str); buf.length() > 0 && buf.charAt(0) == leadingCharacter; buf.deleteCharAt(0))
			;
		return buf.toString();
	}

	public static String trimTrailingCharacter(String str, char trailingCharacter) {
		if (isEmpty(str))
			return str;
		StringBuffer buf;
		for (buf = new StringBuffer(str); buf.length() > 0 && buf.charAt(buf.length() - 1) == trailingCharacter; buf
				.deleteCharAt(buf.length() - 1))
			;
		return buf.toString();
	}

	public static boolean startsWithIgnoreCase(String str, String prefix) {
		if (str == null || prefix == null)
			return false;
		if (str.startsWith(prefix))
			return true;
		if (str.length() < prefix.length()) {
			return false;
		} else {
			String lcStr = str.substring(0, prefix.length()).toLowerCase();
			String lcPrefix = prefix.toLowerCase();
			return lcStr.equals(lcPrefix);
		}
	}

	public static boolean endsWithIgnoreCase(String str, String suffix) {
		if (str == null || suffix == null)
			return false;
		if (str.endsWith(suffix))
			return true;
		if (str.length() < suffix.length()) {
			return false;
		} else {
			String lcStr = str.substring(str.length() - suffix.length()).toLowerCase();
			String lcSuffix = suffix.toLowerCase();
			return lcStr.equals(lcSuffix);
		}
	}

	public static int countOccurrencesOf(String str, String sub) {
		if (str == null || sub == null || str.length() == 0 || sub.length() == 0)
			return 0;
		int count = 0;
		int pos = 0;
		for (int idx = 0; (idx = str.indexOf(sub, pos)) != -1;) {
			count++;
			pos = idx + sub.length();
		}

		return count;
	}

	public static boolean equals(String str1, String str2) {
		return str1 != null ? str1.equals(str2) : str2 == null;
	}

	public static boolean equalsIgnoreCase(String str1, String str2) {
		return str1 != null ? str1.equalsIgnoreCase(str2) : str2 == null;
	}

	public static String capitalize(String str) {
		int len;
		if (str == null || (len = str.length()) == 0)
			return str;
		else
			return (new StringBuffer(len)).append(Character.toUpperCase(str.charAt(0))).append(str.substring(1))
					.toString();
	}

	public static String uncapitalize(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0)
			return str;
		else
			return (new StringBuffer(strLen)).append(Character.toLowerCase(str.charAt(0))).append(str.substring(1))
					.toString();
	}

	public static String parseColumnName2PropertyName(String columnName) {
		StringBuffer sb = new StringBuffer();
		boolean flag = false;
		columnName = columnName.toLowerCase();
		for (int i = 0; i < columnName.length(); i++) {
			char ch = columnName.charAt(i);
			if (ch == '_')
				flag = true;
			else if (flag) {
				sb.append(Character.toUpperCase(ch));
				flag = false;
			} else {
				sb.append(ch);
			}
		}

		return sb.toString();
	}

	public static Long longFromDotIPStr(String ipStr) {
		if (ipStr == null)
			throw new IllegalArgumentException();
		StringBuffer sb = new StringBuffer();
		String tmp[] = null;
		Long rst = null;
		tmp = ipStr.split("\\.");
		if (tmp.length != 4)
			return null;
		for (int i = 0; i < tmp.length; i++)
			if (tmp[i].length() == 1)
				sb.append("00").append(tmp[i]);
			else if (tmp[i].length() == 2)
				sb.append('0').append(tmp[i]);
			else if (tmp[i].length() == 3)
				sb.append(tmp[i]);
			else
				return null;

		try {
			rst = Long.valueOf(sb.toString());
		} catch (NumberFormatException e) {
			return null;
		}
		return rst;
	}

	/**
	 * 将对象转换为字符串 如果该对象为空，则返回null
	 * 
	 * @param obj
	 * @return
	 */
	public static String ObjectToString(Object obj) {
		if (obj != null)
			return obj.toString();
		else
			return null;
	}

	/**
	 * 将对象转换为字符串 如果该对象为空，则转为空字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String ObjectToStringNullToString(Object obj) {
		if (obj != null)
			return obj.toString();
		else
			return "";
	}

	public static Long addLongToString(String value, Long increment) {
		Long v = Long.valueOf(value);
		Long rst = Long.valueOf(v.longValue() + increment.longValue());
		return rst;
	}

	public static Long addStringToLong(Long value, String increment) {
		Long v = Long.valueOf(increment);
		Long rst = Long.valueOf(v.longValue() + value.longValue());
		return rst;
	}

	public static String handleNULL(String input) {
		return handleNULL(input, "");
	}

	public static String handleNULL(String input, String def) {
		if (input == null || input.trim().length() <= 0 || "".equals(input))
			return def;
		else
			return input.trim();
	}

	public static boolean contains(String a[], String s) {
		boolean bRetVal = false;
		for (int i = 0; i < a.length; i++) {
			if (!a[i].trim().equals(s.trim()))
				continue;
			bRetVal = true;
			break;
		}

		return bRetVal;
	}

	public static String convertA2BEncoding(String input, String fromEncoding, String toEncoding)
			throws UnsupportedEncodingException {
		return input != null ? new String(input.trim().getBytes(fromEncoding), toEncoding) : "";
	}

	private static boolean isEmptyArray(Object array[]) {
		return array == null || array.length == 0;
	}

	@SuppressWarnings("unchecked")
	private static boolean isEmptyMap(Map map) {
		return map == null || map.isEmpty();
	}

	/**
	 * null字符串转换成""，如果不为null则返回原值
	 * 
	 * @param value
	 * @return
	 */
	public final static String nullToEmpty(String value) {
		if (isEmpty(value)) {
			return "";
		}
		return value;
	}

	/**
	 * 生成随机字符串
	 * 
	 * @param length
	 *            表示生成字符串的长度
	 * @return
	 */
	public static String randomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 将字符串转换成数组
	 * 
	 * @param string
	 *            String
	 * @param split
	 *            分隔符
	 * @return
	 */
	public static String[] stringToArray(String string, String split) {
		StringTokenizer stString = new StringTokenizer(string, split);
		String[] array = new String[stString.countTokens()];
		int i = 0;
		while (stString.hasMoreTokens()) {
			array[i++] = stString.nextToken();
		}
		return array;
	}

	/**
	 * 将字符串转换成Map
	 * 
	 * @param string
	 *            String
	 * @param split
	 *            分隔符
	 * @return
	 */
	public static Map<String, String> stringToMap(String string, String split) {
		StringTokenizer stString = new StringTokenizer(string, split);
		Map<String, String> map = new HashMap<String, String>();
		while (stString.hasMoreTokens()) {
			String id = stString.nextToken();
			map.put(id, id);
		}
		return map;
	}

	/**
	 * 
	 * 将字符串数组 String[]转换为逗号分隔的字符串
	 * 
	 * @param fid的String
	 *            []
	 * @return
	 */
	public static String arrayToString(String[] fid) {
		String serperater = ",";
		return arrayToString(fid, serperater);
	}

	public static String arrayToString(String[] fid, String serperater) {
		String strfid = "";
		if (fid != null && fid.length > 0) {
			for (int i = 0; i < fid.length; i++) {
				if (fid[i] != null && fid[i].length() > 0) {
					strfid += fid[i] + serperater;
				}
			}
			if (strfid.length() > 0)
				strfid = strfid.substring(0, strfid.length() - 1);
		}
		return strfid;
	}

	/**
	 * 将整数格式化为指定长度的字符串
	 * 
	 * @param int
	 *            intSeq 需要格式化的正整数
	 * @param int
	 *            length 目标字符串的长度
	 * @return
	 */
	public static String formatStringByLength(int intSeq, int length) {
		// 0 代表前面补充0
		// 4 代表长度为4
		// d 代表参数为正数型
		return String.format("%0" + length + "d", intSeq);
	}

	/**
	 * 将数组转换成 以逗号隔开的字符串 用于sql in () 语句:例如： '11','111','111'
	 * 
	 * @param str
	 * @return
	 */
	public static String getInSqlWhere(String[] str) {
		String retStr = "";
		for (int i = 0; i < str.length; i++) {
			if (str[i] != null && str[i].length() > 0)
				retStr += "'" + str[i] + "'" + ",";
		}
		return retStr.length() > 0 ? retStr.substring(0, retStr.lastIndexOf(",")) : retStr;
	}

	/**
	 * xml特殊字符过滤
	 * 
	 * @param value
	 * @return
	 */
	public static String filter(String value) {
		if (value == null)
			return null;
		char content[] = new char[value.length()];
		value.getChars(0, value.length(), content, 0);
		StringBuffer result = new StringBuffer(content.length + 50);
		for (int i = 0; i < content.length; i++)
			switch (content[i]) {
			case 60: // '<'
				result.append("&lt;");
				break;

			case 62: // '>'
				result.append("&gt;");
				break;

			case 38: // '&'
				result.append("&amp;");
				break;

			case 34: // '"'
				result.append("&quot;");
				break;

			case 39: // '\''
				result.append("&#39;");
				break;

			default:
				result.append(content[i]);
				break;
			}

		return result.toString();
	}

	/**
	 * 将字符串转为unicode 编码字符
	 * 
	 * @param str
	 * @return
	 */
	public static String toUnicode(String str) {
		char[] arChar = str.toCharArray();
		int iValue = 0;
		String uStr = "";
		for (int i = 0; i < arChar.length; i++) {
			iValue = str.charAt(i);
			if (iValue <= 256) {
				uStr += "\\u00" + Integer.toHexString(iValue);
			} else {
				uStr += "\\u" + Integer.toHexString(iValue);
			}
		}
		return uStr;
	}

	/**
	 * unicode字符串转中文
	 * 
	 * @param dataStr
	 * @return
	 */
	private static String decodeUnicode(final String dataStr) {
		int start = 0;
		int end = 0;
		final StringBuffer buffer = new StringBuffer();
		while (start > -1) {
			end = dataStr.indexOf("\\u", start + 2);
			String charStr = "";
			if (end == -1) {
				charStr = dataStr.substring(start + 2, dataStr.length());
			} else {
				charStr = dataStr.substring(start + 2, end);
			}
			// System.out.println(end + ":" + charStr);
			char letter = (char) Integer.parseInt(charStr, 16);
			buffer.append(new Character(letter).toString());
			start = end;
		}
		return buffer.toString();
	}

	public static void main(String[] args) {

		try {
			String encodeStr = toUnicode("日志打印机  8051发送打印字符串错");
			System.out.println(encodeStr);

			String strMesg = decodeUnicode(encodeStr);
			System.out.println(strMesg);

		} catch (Exception e) {

		}
	}

	/**
	 * 将字符转为2进制数据
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
			// if (n<b.length-1) hs=hs+":";
		}
		return hs.toUpperCase();
	}

	public static String ipPortHexToUrl(String string) {
		String Ip = string;
		int i = Integer.parseInt(Ip.substring(0, 2), 16);
		int j = Integer.parseInt(Ip.substring(2, 4), 16);
		int k = Integer.parseInt(Ip.substring(4, 6), 16);
		int l = Integer.parseInt(Ip.substring(6, 8), 16);
		int m = Integer.parseInt(Ip.substring(8), 16);
		StringBuffer sss = new StringBuffer("");
		sss.append(i);
		sss.append(".");
		sss.append(j);
		sss.append(".");
		sss.append(k);
		sss.append(".");
		sss.append(l);
		sss.append(":");
		sss.append(m);
		return sss.toString();
	}

	public static String ipPortToHex(String ip, int port) {
		StringTokenizer token = new StringTokenizer(ip, ".");
		int ip1 = Integer.parseInt(token.nextToken().trim());
		int ip2 = Integer.parseInt(token.nextToken().trim());
		int ip3 = Integer.parseInt(token.nextToken().trim());
		int ip4 = Integer.parseInt(token.nextToken().trim());
		String result = (new StringBuilder(String.valueOf(toHex(ip1, 2)))).append(toHex(ip2, 2)).append(toHex(ip3, 2))
				.append(toHex(ip4, 2)).append(Integer.toHexString(port)).toString();
		return result;
	}

	private static String toHex(int num, int atLeastBit) {
		String hex = Integer.toHexString(num);
		int length = hex.length();
		if (length >= atLeastBit)
			return hex;
		for (int i = 0; i < atLeastBit - length; i++)
			hex = (new StringBuilder("0")).append(hex).toString();

		return hex;
	}

	/***
	 * 将 s 进行转码为BASE64 编码的字符串
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public static String getBASE64(String s) {
		if (s == null)
			return null;
		try {
			return (new sun.misc.BASE64Encoder()).encode(s.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*** 将 BASE64 编码的字符串 s 进行解码 */
	public static String getFromBASE64(String s) {
		if (s == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b, "UTF-8");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 拼接 or 语句
	 * 
	 * @param dataList
	 *            数据List
	 * @param columnName
	 *            列名
	 * @return
	 */
	public static String getOrSql(List<String> dataList, String columnName) {
		if (dataList != null && !dataList.isEmpty() && StringUtil.isNotEmpty(columnName)) {
			StringBuffer sql = new StringBuffer();
			for (String data : dataList) {
				sql.append(columnName).append("='").append(data).append("' or ");
			}
			sql.append("1=0");
			return sql.toString();
		}
		return "";
	}

	/**
	 * 拼接 or 语句
	 * 
	 * @param data
	 *            数据数组
	 * @param columnName
	 *            列名
	 * @return
	 */
	public static String getOrSql(String[] data, String columnName) {
		if (data.length > 0) {
			return StringUtil.getOrSql(Arrays.asList(data), columnName);
		}
		return "";
	}

	/**
	 * 删除开头分格字符
	 * 
	 * @param path
	 * @param pathSeparator
	 */
	public static String replaceStartsWithSeparator(String path, String pathSeparator) {
		// TODO Auto-generated method stub
		if (StringUtil.startsWithIgnoreCase(path, pathSeparator))
			path = path.replaceFirst(pathSeparator, "");

		return path;
	}

	/**
	 * 替换占位符
	 * 
	 * @param str
	 * @param arr
	 * @return
	 */
	public static String fillStringByArgs(String str, String[] arr) {
		Matcher m = Pattern.compile("\\{(\\d)\\}").matcher(str);
		while (m.find()) {
			str = str.replace(m.group(), arr[Integer.parseInt(m.group(1))]);
		}
		return str;
	}

	/**
	 * 验证邮箱
	 * 
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email) {
		boolean flag = false;
		try {
			String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 验证手机号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean checkMobileNumber(String mobileNumber) {
		boolean flag = false;
		try {
			Pattern regex = Pattern
					.compile("^(((13[0-9])|(17[0-9])|(15([0-9]))|(18[0-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
			Matcher matcher = regex.matcher(mobileNumber);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 把集合中的字符串以，分隔组成一个字符串 例如：strL有三个元素：xxxx yyyy zzzz 结果返回 ”xxxx,yyyy,zzzz“
	 * 
	 * @param strL
	 * @return
	 */
	public static String groupDatas(List<String> strL) {
		// List<String> labelFlags = getPrarmListValue("labelFlag");
		String str = "";
		if (null != strL) {
			for (String s : strL) {
				str += "," + s;
			}
		}else{
			return null;
		}
		
		return str.length() > 0 ? str.substring(1) : str;
	}
	
	/**
	 * 把集合中的字符串以strType分隔组成一个字符串 例如：strL有三个元素：xxxx yyyy zzzz,当strType是,时 结果返回 ”xxxx,yyyy,zzzz“
	 * 
	 * @param strL
	 * @param strType
	 * @return
	 */
	public static String groupDatas(List<String> strL,String strType) {
		// List<String> labelFlags = getPrarmListValue("labelFlag");
		String str = "";
		if (null != strL) {
			for (String s : strL) {
				str += strType + s;
			}
		}else{
			return null;
		}
		
		return str.length() > 0 ? str.substring(strType.length()) : str;
	}

	/**
	 * 指定位置插入字符串
	 * 
	 * @param src
	 * @param dec
	 * @param position
	 * @return
	 */
	public static String insertStringInParticularPosition(String src, String dec, int position) {
		StringBuffer stringBuffer = new StringBuffer(src);

		return stringBuffer.insert(position, dec).toString();

	}
	
	/**
	 * 返回文件后缀
	 * @param url
	 * @return
	 */
	public static String getFileType(String url){
		String type = url.substring(url.lastIndexOf(".")+ 1, url.length()).toLowerCase();
		if(type.matches("jpg|jpeg|bmp|gif|tif|tiff|png"))
			type = "2";//图片
		else if(type.matches("doc|txt|docx|xls|xlsx|pdf"))
			type = "3";//文档
		else if(type.matches("mp4|avi|rmvb|rm|mid|wma"))
			type = "4";//视频
		else
			type = "5";//其他
		return type;
	}
}