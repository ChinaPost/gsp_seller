package com.cpz.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.framework.exception.CommonException;
import com.framework.pojo.Ret;
import com.framework.utils.SpringBeanManger;
import com.framework.utils.StringUtil;
import com.cpz.pojo.ErrorInfo;
import com.cpz.service.ErrorService;

/**
 * @author tang E-mail: killerover84@163.com
 * @version 2016年5月9日 下午1:40:02 类说明
 */
public class CommonUtil {
	private final static Logger logger = LoggerFactory.getLogger(CommonUtil.class);
	private final static ErrorService errorService = (ErrorService) SpringBeanManger.getBean("errorService");

	/**
	 * 统一处理返回前台JSON结果，简化版
	 */
	public static Map ReturnWarp(String errorCode, String errorType) {
		return ReturnWarp(errorCode, errorType, null, null);
	}

	/**
	 * 统一处理返回前台JSON结果，错误信息是变量
	 */
	public static Map ReturnWarp(String errorCode, String errorType, String errorMsg) {
		Map reltMap = new HashMap();
		try {
			ErrorInfo errorInfo = errorService.getErrorInfo(errorCode, errorType);
			String[] errorMsgs = { errorMsg };
			reltMap.put("ret",
					new Ret(errorInfo.getErrorCode(), StringUtil.fillStringByArgs(errorInfo.getErrorMsg(), errorMsgs)));
		} catch (CommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			reltMap.put("ret", new Ret(Constant.TRAN_COMMONEXCEP, e.getMessage()));
		}

		return reltMap;
	}

	/**
	 * 统一处理返回前台JSON结果，错误信息是变量
	 */
	public static Map ReturnWarp(String errorCode, String errorType, String[] errorMsgs) {
		Map reltMap = new HashMap();
		try {
			ErrorInfo errorInfo = errorService.getErrorInfo(errorCode, errorType);
			reltMap.put("ret",
					new Ret(errorInfo.getErrorCode(), StringUtil.fillStringByArgs(errorInfo.getErrorMsg(), errorMsgs)));
		} catch (CommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			reltMap.put("ret", new Ret(Constant.TRAN_COMMONEXCEP, e.getMessage()));
		}

		return reltMap;
	}

	/**
	 * 统一异常处理返回前台JSON结果
	 * 
	 * @param e
	 * @return
	 */
	public static Map<String, Object> ReturnWarp(Exception e) {
		// TODO Auto-generated method stub
		if (e instanceof CommonException) {
			CommonException comExc = (CommonException) e;
			if (!StringUtil.isBlank(comExc.getErrorCode()) && !StringUtil.isBlank(comExc.getErrorType())) {
				if (null == comExc.getParams() || comExc.getParams().length <= 0) {
					return ReturnWarp(comExc.getErrorCode(), comExc.getErrorType());
				} else {
					return ReturnWarp(comExc.getErrorCode(), comExc.getErrorType(), comExc.getParams());
				}
			}
		}

		return ReturnWarp(Constant.TRAN_COMMONEXCEP, null, e.getMessage(), null);
	}

	/**
	 * 统一处理返回前台JSON结果
	 */
	public static Map ReturnWarp(String errorCode, String errorType, String errorMsg, Map relt) {
		Map reltMap = new HashMap();
		try {
			if (StringUtil.isBlank(errorCode)) {
				if (StringUtil.isBlank(errorMsg)) {
					ErrorInfo errorInfo = errorService.getErrorInfo(Constant.TRAN_UNKONW, Constant.ERRORTYPE);
					if (null != errorInfo)
						reltMap.put("ret", new Ret(errorInfo.getErrorCode(), errorInfo.getErrorMsg()));
					else
						reltMap.put("ret", new Ret(Constant.TRAN_SYSERR, SpringBeanManger.getTextValue("TRAN_SYSERR")));
				} else
					reltMap.put("ret", new Ret(Constant.TRAN_UNKONW, errorMsg));
			} else if (Constant.TRAN_SUCCESS.equals(errorCode)) {
				ErrorInfo errorInfo = errorService.getErrorInfo(errorCode, errorType);
				reltMap.put("ret", new Ret(errorInfo.getErrorCode(), errorInfo.getErrorMsg()));
				if (null != relt)
					reltMap.putAll(relt);
			} else {
				if (StringUtil.isBlank(errorMsg)) {
					ErrorInfo errorInfo = errorService.getErrorInfo(errorCode, errorType);
					reltMap.put("ret", new Ret(errorInfo.getErrorCode(), errorInfo.getErrorMsg()));
				} else {
					reltMap.put("ret", new Ret(errorCode, errorMsg));
				}

			}
		} catch (CommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			reltMap.put("ret", new Ret(Constant.TRAN_COMMONEXCEP, e.getMessage()));
		}

		return reltMap;
	}

	/**
	 * 生成UUID
	 * 
	 * @return
	 */
	public static String getUUID() {
		// TODO Auto-generated method stub
		return UUID.randomUUID().toString().replace("-", "");
	}

}
