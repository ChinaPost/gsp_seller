package com.cpz.service;

import com.framework.exception.CommonException;
import com.cpz.pojo.ErrorInfo;

/** 
* @author tang E-mail: killerover84@163.com
* @version 2016年5月9日 下午1:48:48 
* 类说明 
*/
public interface ErrorService {

	/**
	 * 根据错误码，错误类型 获取错误信息
	 * @param string
	 * @param string2
	 * @return
	 */
	ErrorInfo getErrorInfo(String errorCode, String errorType) throws CommonException;

}
