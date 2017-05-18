package com.cpz.controller.front;

import java.util.Locale;
import java.util.Map;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.support.RequestContext;

import com.framework.controller.BaseController;
import com.framework.utils.SpringBeanManger;
import com.cpz.utils.CommonUtil;
import com.cpz.utils.Constant;

/**
 * @author tang E-mail: killerover84@163.com
 * @version 2016年5月16日 上午9:05:38 类说明
 */
@Controller
@Scope("prototype")
@RequestMapping("/front/i18n")
public class I18n extends BaseController {

	// 国际化
	@RequestMapping("/inter.do")
	public @ResponseBody Map<String, Object> inter(@RequestParam Map reqMap) {

		if (null == reqMap || reqMap.isEmpty())
			return CommonUtil.ReturnWarp(Constant.TRAN_PARAERCODE, Constant.ERRORTYPE);

		String langType = reqMap.get("langType") == null ? null : reqMap.get("langType").toString();
		if (null == langType || langType.isEmpty())
			langType = "zh";

		if (langType.equals("zh")) {
			Locale locale = new Locale("zh", "CN");
			getRequest().getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
		} else if (langType.equals("en")) {
			Locale locale = new Locale("en", "US");
			getRequest().getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
		} else
			getRequest().getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,
					LocaleContextHolder.getLocale());

		// 从后台代码获取国际化信息
		RequestContext requestContext = new RequestContext(getRequest());
		System.out.println(requestContext.getMessage("xxx"));
		System.out.println(SpringBeanManger.getTextValue("xxx"));

		return CommonUtil.ReturnWarp(Constant.TRAN_SUCCESS, Constant.ERRORTYPE);
	}
}
