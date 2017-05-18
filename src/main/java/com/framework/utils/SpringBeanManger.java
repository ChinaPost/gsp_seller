package com.framework.utils;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * @author tang E-mail: killerover84@163.com
 * @version 2016年4月14日 下午7:22:10 类说明 加到spring容器中的所以bean
 */
public class SpringBeanManger implements ApplicationContextAware {

	static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		synchronized (applicationContext) {
			context = applicationContext;
		}
	}

	public static void setAppContext(ApplicationContext applicationContext) {
		context = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return context;
	}

	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}

	/*
	 * 取国际化值
	 */
	public static String getTextValue(String key) {
		HttpServletRequest  request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return getApplicationContext().getMessage(key, null, null,(Locale)request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME));
	}
	
	/*
	 * 取session
	 */
	public static Object getSession(String key) {
		HttpServletRequest  request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getSession().getAttribute(key);
//		return getApplicationContext().getMessage(key, null, null,(Locale)request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME));
	}

}
