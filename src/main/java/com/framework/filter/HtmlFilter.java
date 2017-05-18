package com.framework.filter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.util.Assert;
//import org.springframework.web.util.HtmlUtils;

/**
 * @ClassName: HtmlFilter
 * @Description: html转义过滤器
 * @author: 阿豪
 * @date: 2016-08-20
 *
 */
public class HtmlFilter implements Filter {

	//排除列表
	private static String contextPath = "";
	private static Map<String,Set<String>> excludeMap = new HashMap<String,Set<String>>();
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//工程名后面的uri字符串(需要重启server)
		HtmlFilter.contextPath=filterConfig.getServletContext().getContextPath();
		
/**
		示例1:排除一个接口
		excludeMap.put(contextPath+"/shop/course/addComment.do" , null);
		excludeMap.put(contextPath+"/shop/course/addCommentReply.do" , null);
		
		示例2:排除某接口的多个字段
		Set set = new HashSet();
		set.add("answerInfo1");//字段1
		set.add("answerInfo2");//字段2
		excludeMap.put(contextPath+"/shop/course/addCommentReply222.do" , set);
 */
		
		loadConfigXml(filterConfig.getServletContext());
	}
	
	
	
	//加载配置xml
	private void loadConfigXml(ServletContext servletContext) {
		// TODO Auto-generated method stub
		
		String xmlPath = servletContext.getClassLoader().getResource("htmlFilter.xml").getPath();
		Document document = null;  
		try {
			SAXReader saxReader = new SAXReader();
			document = saxReader.read(new File(xmlPath)); // 读取XML文件,获得document对象
			
			Element root = document.getRootElement();
			List<Element> interfaceList = root.elements();
	        for (Element inf : interfaceList) {
	        	
	        	Set set = null;
	        	Element uri = inf.element("uri");
	        	Element fields = inf.element("fields");
	        	
	        	if(fields!=null){
	        		set = new HashSet();
	        		List<Element> fieldList = fields.elements("field");
	        		for (Element field : fieldList) {
	        			set.add(field.getText());//字段
					}
	        	}
	        	
	        	excludeMap.put( HtmlFilter.contextPath + uri.getText() , set );
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}



	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		String requestURI = request.getRequestURI();
		if(excludeMap.containsKey(requestURI)){
			//排除
			Set<String> excludeFields = excludeMap.get(requestURI);
			if( excludeFields==null || excludeFields.isEmpty() ){//如果排除接口
				chain.doFilter(request, response);
			}else{//如果是排除字段
				MyHtmlRequest myrequest = new MyHtmlRequest(request,excludeFields); 
				chain.doFilter(myrequest, response);
			}
				
				
		}else{
			//过滤
			MyHtmlRequest myrequest = new MyHtmlRequest(request); 
			chain.doFilter(myrequest, response);
		}

	}

	@Override
	public void destroy() {

	}


}//HtmlFilter



/**
 * @ClassName: MyHtmlRequest
 * @Description: 使用Decorator模式包装request对象，实现html标签转义功能
 * @author: 阿豪
 * @date: 2016-08-20
 *
 */
class MyHtmlRequest extends HttpServletRequestWrapper {

	private HttpServletRequest request;
	private Set<String> excludeFields;

	public MyHtmlRequest(HttpServletRequest request) {
		super(request);
		this.request = request;
	}

	public MyHtmlRequest(HttpServletRequest request , Set<String> excludeFields ) {
		super(request);
		this.request = request;
		this.excludeFields = excludeFields;
	}
	
	/*
	 * 覆盖需要增强的getParameter方法
	 * 
	 * @see javax.servlet.ServletRequestWrapper#getParameter(java.lang.String)
	 */
	@Override
	public String getParameter(String name) {
		String value = this.request.getParameter(name);
		if (value == null) {
			return null;
		}
		// 调用filter转义value中的html标签
//		return filter(value);
		
		if( this.excludeFields == null || this.excludeFields.contains(name)==false ){
			//排除字段为空(所有需要过滤)，或者当不包含当前字段(需要过滤)
			return htmlEscape(value, "UTF-8"); //使用spring的工具类吧。。
		}
		
		return value;
	}

	@Override
	public String[] getParameterValues(String name) {
		// TODO Auto-generated method stub
//		return super.getParameterValues(name);
		String[] values = this.request.getParameterValues(name);
		if (values == null) {
			return null;
		}
		if( this.excludeFields == null || this.excludeFields.contains(name)==false ){
			//排除字段为空(所有需要过滤)，或者当不包含当前字段(需要过滤)
			for (int i = 0; i < values.length; i++) {
				values[i] = htmlEscape(values[i], "UTF-8"); //使用spring的工具类吧。。
			}
		}
		return values;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		// TODO Auto-generated method stub
//		return super.getParameterMap();
		Map<String, String[]> parameterMap = this.request.getParameterMap();
		Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
		Map<String, String[]> map = new HashMap<String, String[]>();
		//
		for (Entry<String, String[]> entry : entrySet) {
			String key = entry.getKey();
			String[] values = entry.getValue();
			if( this.excludeFields == null || this.excludeFields.contains(key)==false ){ 
				//排除字段为空(所有需要过滤)，或者当不包含当前字段(需要过滤)
				for (int i = 0; i < values.length; i++) {
					values[i] = htmlEscape(values[i], "UTF-8"); //使用spring的工具类吧。。
				}
			}
			map.put(key, values);
		}
		//
		return map;
	}

	
	
	/**
	 * @Method: filter
	 * @Description: 过滤内容中的html标签
	 * @Anthor:孤傲苍狼
	 * @param message
	 * @return
	 */
//	public String filter(String message) {
//		if (message == null) {
//			return null;
//		}
//		char content[] = new char[message.length()];
//		message.getChars(0, message.length(), content, 0);
//		StringBuffer result = new StringBuffer(content.length + 50);
//		for (int i = 0; i < content.length; i++) {
//			switch (content[i]) {
//			case '<':
//				result.append("&lt;");
//				break;
//			case '>':
//				result.append("&gt;");
//				break;
//			case '&':
//				result.append("&amp;");
//				break;
//			case '"':
//				result.append("&quot;");
//				break;
//			default:
//				result.append(content[i]);
//			}
//		}
//		return result.toString();
//	}
	
	
	/**
	 * Turn special characters into HTML character references.
	 * Handles complete character set defined in HTML 4.01 recommendation.
	 * <p>Escapes all special characters to their corresponding
	 * entity reference (e.g. {@code &lt;}) at least as required by the
	 * specified encoding. In other words, if a special character does
	 * not have to be escaped for the given encoding, it may not be.
	 * <p>Reference:
	 * <a href="http://www.w3.org/TR/html4/sgml/entities.html">
	 * http://www.w3.org/TR/html4/sgml/entities.html
	 * </a>
	 * @param input the (unescaped) input string
	 * @param encoding The name of a supported {@link java.nio.charset.Charset charset}
	 * @return the escaped string
	 * @since 4.1.2
	 */
	public static String htmlEscape(String input, String encoding) {
		Assert.notNull(encoding, "Encoding is required");
		if (input == null) {
			return null;
		}
		StringBuilder escaped = new StringBuilder(input.length() * 2);
		for (int i = 0; i < input.length(); i++) {
			char character = input.charAt(i);
			String reference = null;
			switch (character){
			case '<':
				reference= "&lt;";
			case '>':
				reference= "&gt;";
			case '(':
				reference= "&#40;";
			case ')':
				reference= "&#41;";
			case ';':
				reference= "&#59;";
				
//			case '"':
//				reference= "&quot;";
//			case '&':
//				reference= "&amp;";
//			case '\'':
//				reference= "&#39;";
		}
			if (reference != null) {
				escaped.append(reference);
			}
			else {
				escaped.append(character);
			}
		}
		return escaped.toString();
	}
	
	
	
}