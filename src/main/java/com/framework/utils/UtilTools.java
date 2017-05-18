package com.framework.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;

/**
 * @author tang E-mail: killerover84@163.com
 * @version 2016年5月9日 上午9:26:20 类说明
 */
public class UtilTools {
	public static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * * 包装成树形结构 (全部属性)
	 * 
	 * @param tree
	 * @return
	 * @throws Exception
	 */
	public static List factorTree(List tree) throws Exception {
		if (tree != null) {
			List t_list = new ArrayList();
			Map map = new HashMap();
			for (Object o : tree) {
				Class clazz = o.getClass();
				Field id = clazz.getDeclaredField("id");
				if (!id.isAccessible()) {
					id.setAccessible(true);
				}
				String Id = id.get(o).toString();
				map.put(Id, o);
			}
			for (Object o : map.keySet()) {
				String cId = (String) o;
				Object obj = map.get(cId);
				Class clazz = obj.getClass();
				Field pId = clazz.getDeclaredField("parentId");
				if (!pId.isAccessible()) {
					pId.setAccessible(true);
				}
				String id = (String) pId.get(obj);

				if (id == null) {
					t_list.add(obj);
				} else {
					Object object = map.get(id);
					Class clazz1 = object.getClass();
					Field children = clazz1.getDeclaredField("children");
					if (!children.isAccessible()) {
						children.setAccessible(true);
					}
					List list = (List) children.get(object);
					if (CollectionUtils.isEmpty(list)) {
						list = new ArrayList();
					}
					list.add(obj);
					children.set(object, list);
				}
			}
			return t_list;
		}
		return null;
	}

	/**
	 * 转化为整数，当为空时返回0
	 * 
	 * @param object
	 * @return
	 */
	public static int parseIntWhenNullRetur0(Object object) {
		// TODO Auto-generated method stub
		if (null == object || StringUtil.isBlank(object.toString()))
			return 0;

		return Integer.parseInt(object.toString());
	}

	/**
	 * 转化为长整数，当为空时返回0l
	 * 
	 * @param string
	 * @return
	 */
	public static Long parseLongWhenNullRetur0(Object object) {
		// TODO Auto-generated method stub
		if (null == object || StringUtil.isBlank(object.toString()))
			return 0l;

		return Long.parseLong(object.toString());
	}

	/**
	 * 转化为双浮点数，当为空时返回0.0
	 * 
	 * @param string
	 * @return
	 */
	public static Double parseDoubleWhenNullRetur0(Object object) {
		// TODO Auto-generated method stub
		if (null == object || StringUtil.isBlank(object.toString()))
			return 0.0;

		return Double.parseDouble(object.toString());
	}

	public static Date parseDateWhenNullRetur0(Object object) {
		// TODO Auto-generated method stub
		if (null == object || StringUtil.isBlank(object.toString()))
			return null;

		try {
			String formatStr = null;
			if (object.toString().indexOf(":") != -1) {
				formatStr = "yyyy-MM-dd HH:mm:ss";
			} else {
				formatStr = "yyyy-MM-dd";
			}
			SimpleDateFormat df = new SimpleDateFormat(formatStr);
			return df.parse(object.toString());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取客户端IP
	 * 
	 * @param req
	 * @return
	 */
	public static String getIpAddr(HttpServletRequest req) {
		String ip = req.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = req.getRemoteAddr();
		}
		String[] ipArr = ip.split(",");
		return ipArr[0];
	}

	/**
	 * Map --> Bean : 利用org.apache.commons.beanutils 工具类实现 Map --> Bean
	 * 
	 * @param map
	 * @param obj
	 */
	public static void transMap2Bean(Map<String, Object> map, Object obj) {
		ConvertUtils.register(new Converter() {

			@Override
			public Object convert(Class type, Object value) {
				// TODO Auto-generated method stub
				if (value == null || value.toString().trim().length() == 0) {
					return null;
				}
				try {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					return df.parse(value.toString().trim());
				} catch (Exception e) {
					try {
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						return df.parse(value.toString().trim());
					} catch (ParseException ex) {
						return null;
					}
				}
			}
		}, Date.class);
		if (map == null || obj == null) {
			return;
		}
		try {
			BeanUtils.populate(obj, map);
		} catch (Exception e) {
			System.out.println("transMap2Bean2 Error " + e);
		}
	}

	/**
	 * Bean --> Map : 利用Introspector和PropertyDescriptor 将Bean --> Map
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> transBean2Map(Object obj) {

		if (obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();

				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(obj);

					map.put(key, value);
				}

			}
		} catch (Exception e) {
			System.out.println("transBean2Map Error " + e);
		}

		return map;

	}

	/**
	 * 对象名+Info当key,对象当value
	 * 
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> obj2MapWithObjName(Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();

		String key = obj.getClass().getSimpleName().substring(0, 1).toLowerCase()
				+ obj.getClass().getSimpleName().substring(1);

		map.put(key + "Info", obj);

		return map;
	}

	/**
	 * 生成时间加四位随机数的码
	 * 
	 * @return
	 */
	public static String genDateAndFourRandomCode() {
		String code = DateUtil.getDateStr(null) + RandomStringUtils.randomNumeric(4);

		return code;
	}
	
	/**
	 * 生成指定长度的随机数(含大小写,数字)
	 * 后4位随机生成数字
	 * @param length
	 * @return
	 */
	public static String generateRandomString(int length) { 
		StringBuffer sb = new StringBuffer(); 
		Random random = new Random(); 
		for (int i = 0; i < length-4; i++) {
			sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length()))); 
		}
		sb.append(RandomStringUtils.randomNumeric(4));
		return sb.toString(); 
	} 
}
