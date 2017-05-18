package com.framework.controller;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.framework.exception.CommonException;
import com.framework.utils.SpringBeanManger;
import com.framework.utils.UtilTools;

/**
 * @author tang E-mail: killerover84@163.com
 * @version 2016年4月14日 下午2:48:39 类说明
 *          1、所有action的基类，提供获取request,response,session...常用对像 2、封装了取得页面传递参数方法
 *          3、封装了对像与map之间的赋值
 */
public class BaseController {
	private HttpServletRequest request;
	private HttpServletResponse response;

	@ModelAttribute
	public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getPraramValue(String paramName, String defaultValue) {
		if (StringUtils.isBlank(paramName)) {
			return defaultValue;
		}
		String paramValue = getRequest().getParameter(paramName);
		paramValue = StringUtils.defaultIfEmpty(paramValue, defaultValue);
		return paramValue;
	}

	public Long getPraramLongValue(String paramName, Long defaultValue) {
		String paramValue = getPraramValue(paramName, defaultValue + "");

		if (StringUtils.isNumeric(paramValue)) {
			return Long.valueOf(paramValue);
		}

		return defaultValue;
	}

	public Integer getPraramIntValue(String paramName, Integer defaultValue) {
		String paramValue = getPraramValue(paramName, defaultValue + "");
		// isNumeric不能用在Double的判断
		if (StringUtils.isNumeric(paramValue)) {
			return Integer.valueOf(paramValue);
		}

		return defaultValue;
	}

	public Double getPraramDoubleValue(String paramName, Double defaultValue) {
		String paramValue = getPraramValue(paramName, defaultValue + "");
		try {
			Double rltDouble = Double.parseDouble(paramValue);
			return rltDouble;
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return defaultValue;
	}

	/**
	 * 获取请求参数
	 * 
	 * @param paramName
	 *            参数名
	 * @return String类型数组（无此参数时返回null）
	 */
	public String[] getPrarmArrValue(String paramName) {
		String[] prarmArr;
		if (StringUtils.isBlank(paramName)) {
			return null;
		}
		prarmArr = getRequest().getParameterValues(paramName);
		return prarmArr;
	}

	/**
	 * 获取请求参数数组
	 * 
	 * @param req
	 * @param paramName
	 *            参数名
	 * @return List数组（无此参数时返回null）
	 */

	public List<String> getPrarmListValue(String paramName) {
		String[] prarmArr = getPrarmArrValue(paramName);
		if (prarmArr == null || StringUtils.isBlank(paramName))
			return null;
		List<String> list = new ArrayList<String>();
		for (String string : prarmArr) {
			list.add(string);
		}
		// if(list.size() == 0) list.add("");//给空串
		return list;
	}

	/**
	 * 获取请求参数数组（指定返回List的大小，不足时补空串）
	 * 
	 * @param req
	 * @param paramName
	 *            参数名
	 * @param defaultSize
	 *            指定返回List的大小。如果为0则只返回空List
	 * @return List数组（defaultSize大小）
	 */
	public List<String> getPrarmListValue(String paramName, int defaultSize) {
		List<String> list = getPrarmListValue(paramName);
		return getListValueDef(list, defaultSize, "");
	}

	/**
	 * 
	 * 获取请求参数数组（指定返回List的大小，不足时补空串）
	 * 
	 * @param req
	 * @param paramName
	 *            参数名
	 * @param defaultSize
	 *            指定返回List的大小。如果为0则只返回空List
	 * @param defaultStr
	 *            默认值
	 * @return List数组（defaultSize大小）
	 */
	public List<String> getPrarmListValue(String paramName, int defaultSize, String defaultStr) {
		List<String> list = getPrarmListValue(paramName);
		return getListValueDef(list, defaultSize, "");
	}

	/**
	 * 用 defaultStr 按 defaultSize 填充List。
	 * 如果defaultSize小于List.size取List子串，如果如果defaultSize大于List.size，用defaultStr填充
	 * 
	 * @param list
	 * @param defaultSize
	 *            指定结果的List大小。如果为0则只返回空List
	 * @param defaultStr
	 *            默认值
	 * @return List数组（defaultSize大小）
	 */
	public List<String> getListValueDef(List<String> list, int defaultSize, String defaultStr) {
		if (list == null)
			list = new ArrayList<String>();
		int size = list.size();
		if (defaultSize <= 0) {
			return list;
		}
		if (defaultSize < size) {
			return list.subList(0, defaultSize);
		} else if (defaultSize == size) {
			return list;
		} else {
			for (int i = 0; i < defaultSize - size; i++) {
				list.add(defaultStr == null ? "" : defaultStr);// 不足指定值时，增加空串
			}
		}
		return list;
	}

	/**
	 * 将Request中的请求属性填充到bean中的同名字段 请求为数组的填充到Bean的相应List中
	 * 
	 * @param bean
	 * @param request
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void putAllReqParamsToVo(Object bean) throws Exception {
		if (bean == null)
			return;

		Class clazz = bean.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) { // 按定义的字段作为遍历基准
			String fieldName = field.getName();
			if ("java.lang.String".equals(field.getType().getName())) { // 过滤无效字段
				String value = getPraramValue(fieldName, "");
				if (!"".equals(value)) {
					if (!Modifier.isFinal(field.getModifiers())) {
						new PropertyDescriptor(field.getName(), clazz).getWriteMethod().invoke(bean,
								String.valueOf(value));
					}
				}
			}
			if ("java.lang.Integer".equals(field.getType().getName())) {
				Integer value = getPraramIntValue(fieldName, 0);
				if (!Modifier.isFinal(field.getModifiers())) {
					new PropertyDescriptor(field.getName(), clazz).getWriteMethod().invoke(bean, value);
				}
			}
			if ("java.lang.Long".equals(field.getType().getName())) {
				Long value = getPraramLongValue(fieldName, 0l);
				if (!Modifier.isFinal(field.getModifiers())) {
					new PropertyDescriptor(field.getName(), clazz).getWriteMethod().invoke(bean, value);
				}
			}
			if ("java.lang.Double".equals(field.getType().getName())) {
				Double value = getPraramDoubleValue(fieldName, 0.00);
				if (!Modifier.isFinal(field.getModifiers())) {
					new PropertyDescriptor(field.getName(), clazz).getWriteMethod().invoke(bean, value);
				}
			}
			// if ("java.util.List".equals(field.getType().getName()) &&
			// field.getGenericType() instanceof ParameterizedType) { // 过滤无效字段
			// 判断为List
			if (field.getType().isAssignableFrom(List.class)) {
				String[] prarmArr = getPrarmArrValue(fieldName);
				// 参数值不为空
				if (prarmArr != null) {
					List<Object> paramList = new ArrayList<Object>();
					// List声明了泛型
					if (field.getGenericType() instanceof ParameterizedType) {
						ParameterizedType pt = (ParameterizedType) field.getGenericType();
						Class<Object> genericClazz = (Class<Object>) pt.getActualTypeArguments()[0]; // 属性中List<Double>
																										// 中的Double
																										// class
						if (genericClazz.isAssignableFrom(Integer.class)) {
							// System.out.println("List<Integer>");
							for (String string : prarmArr) {
								paramList.add(NumberUtils.toInt(string, 0));
							}
						} else if (genericClazz.isAssignableFrom(Double.class)) {
							// System.out.println("List<Double>");
							for (String string : prarmArr) {
								paramList.add(NumberUtils.toDouble(string, 0.00));
							}
						} else if (genericClazz.isAssignableFrom(Long.class)) {
							// System.out.println("List<Long>");
							for (String string : prarmArr) {
								paramList.add(NumberUtils.toLong(string, 0l));
							}
						} else if (genericClazz.isAssignableFrom(String.class)) {
							// System.out.println("List<String>");
							for (String string : prarmArr) {
								paramList.add(string);
							}
						} else {
							// System.out.println("List<> other object");
						}
					} else {
						// System.out.println("List no <>");
						for (String string : prarmArr) {
							paramList.add(string);
						}
					}
					// 通过反射设置属性值
					if (!Modifier.isFinal(field.getModifiers())) {
						new PropertyDescriptor(field.getName(), clazz).getWriteMethod().invoke(bean, paramList);
					}
				}
			} else {
				// System.out.println("not List");
			}

		}
	}

	/**
	 * 将Request中的请求属性填充到bean中的同名字段
	 * 
	 * @param bean
	 * @param request
	 * @throws Exception
	 */
	public <T> T getAllReqParamsVo(Class<T> clazz) throws Exception {
		T bean = clazz.newInstance();
		putAllReqParamsToVo(bean);
		return bean;
	}

	/**
	 * 向页面写入数据
	 * 
	 * @param str
	 */
	public void writeStr(String str) {
		HttpServletResponse response = getResponse();
		response.setContentType("text/plain; charset=utf-8");
		// response.setCharacterEncoding("utf-8");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.write(str);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (pw != null)
				pw.close();
		}
	}

	/**
	 * 重定向
	 * 
	 * @param url
	 */
	public void redirect(String url) {
		try {
			if (url != null && !url.equals("")) {
				response.sendRedirect(url);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	/**
	 * 对字符串进行URLDecoder
	 * 
	 * @param str
	 * @param dec
	 * @return
	 */
	public String decodeSafe(String str, String dec) {
		if (StringUtils.isEmpty(str) || StringUtils.isEmpty(dec)) {
			return str;
		}
		try {
			return URLDecoder.decode(str, dec); // 对于页面的中文参数，已做encode
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}

	/*
	 * 获取servletContext
	 */
	public static final ServletContext getServletContext() {
		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
		ServletContext servletContext = webApplicationContext.getServletContext();

		return servletContext;
	}

	/**
	 * 将Request中的请求属性填充到bean中的同名字段,并返回bean集合
	 * 
	 * @param bean
	 * @param request
	 * @throws Exception
	 */
	public <T> List<T> getAllReqParamsVos(Class<T> clazz) throws Exception {
		List<T> beans = new ArrayList<T>(0);

		int length = 0;
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields){
			String fieldName = field.getName();
			if("id".equals(fieldName))continue;
			length = getPrarmArrValue(fieldName) == null ? 0 : getPrarmArrValue(fieldName).length;
			if(length > 0)break;
		}

		for (int i = 0; i < length; i++) {
			T bean = clazz.newInstance();
			putAllReqParamsToVos(bean, i);
			beans.add(bean);
		}

		return beans;
	}

	/**
	 * 将Request中的请求属性填充到bean中的同名字段 请求为数组的填充到Bean的相应List中 返回bean集合
	 * 
	 * @param bean
	 * @param request
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void putAllReqParamsToVos(Object bean, int index) throws Exception {
		if (bean == null)
			return;

		Class clazz = bean.getClass();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) { // 按定义的字段作为遍历基准
			String fieldName = field.getName();
			if(null == getPrarmListValue(fieldName) || getPrarmListValue(fieldName).size() <= index)continue;
			
			if ("java.lang.String".equals(field.getType().getName())) { // 过滤无效字段
				String value = getPrarmListValue(fieldName).get(index);
				if (!"".equals(value)) {
					if (!Modifier.isFinal(field.getModifiers())) {
						new PropertyDescriptor(field.getName(), clazz).getWriteMethod().invoke(bean,
								String.valueOf(value));
					}
				}
			}
			if ("java.lang.Integer".equals(field.getType().getName())) {
				Integer value = UtilTools.parseIntWhenNullRetur0(getPrarmListValue(fieldName).get(index));
				if (!Modifier.isFinal(field.getModifiers())) {
					new PropertyDescriptor(field.getName(), clazz).getWriteMethod().invoke(bean, value);
				}
			}
			if ("java.lang.Long".equals(field.getType().getName())) {
				Long value = UtilTools.parseLongWhenNullRetur0(getPrarmListValue(fieldName).get(index));
				if (!Modifier.isFinal(field.getModifiers())) {
					new PropertyDescriptor(field.getName(), clazz).getWriteMethod().invoke(bean, value);
				}
			}
			if ("java.lang.Double".equals(field.getType().getName())) {
				Double value = UtilTools.parseDoubleWhenNullRetur0(getPrarmListValue(fieldName).get(index));
				if (!Modifier.isFinal(field.getModifiers())) {
					new PropertyDescriptor(field.getName(), clazz).getWriteMethod().invoke(bean, value);
				}
			}
			if ("java.util.Date".equals(field.getType().getName())) {
				Date value = UtilTools.parseDateWhenNullRetur0(getPrarmListValue(fieldName).get(index));
				if (!Modifier.isFinal(field.getModifiers())) {
					new PropertyDescriptor(field.getName(), clazz).getWriteMethod().invoke(bean, value);
				}
			}
		}
	}
	
	/**
	 * form表单验证
	 * @param entity
	 * @throws CommonException
	 * @throws Exception
	 */
	public static void validators(Object entity) throws CommonException,Exception{
		LocalValidatorFactoryBean d = (LocalValidatorFactoryBean)SpringBeanManger.getBean("validator");
		Validator validator = d.getValidator();    
		
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(entity);    
		for (ConstraintViolation<Object> constraintViolation : constraintViolations) {    
//			System.out.println("对象属性:"+constraintViolation.getPropertyPath());    
//			System.out.println("国际化key:"+constraintViolation.getMessageTemplate()); 
//			System.out.println("错误信息:"+constraintViolation.getMessage());    
			throw new CommonException(constraintViolation.getMessage());
		}
	}
	
	public void setAttribute(String name, Object value) {
		this.getRequest().setAttribute(name, value);
	}
	
	public Object getAttribute(String name) {
		return this.getRequest().getAttribute(name);
	}
	
	public void setSession(String name, Object value){
		this.getRequest().getSession().setAttribute(name, value);
	}
	
	public Object getSession(String name){
		return this.getRequest().getSession().getAttribute(name);
	}
	
	public void removeSession(String name){
		this.getRequest().getSession().removeAttribute(name);
	}
}
