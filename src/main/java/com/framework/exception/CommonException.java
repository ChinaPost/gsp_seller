/**
 * tang
 */
package com.framework.exception;

/**
 * CommonException 框架通用的异常封装类 包括：异常编码、异常详细信息
 * 
 * @author tang
 * @date: 2016-04-18
 */
@SuppressWarnings("serial")
public class CommonException extends RuntimeException {

	protected String errorType;
	protected String errorCode;
	protected String errorMessage;
	protected Throwable cause;
	protected String[] params;

	public CommonException(String errorCode, String errorMessage, Throwable cause) {
		super(errorMessage, cause);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.cause = cause;
	}

	public CommonException(Throwable cause) {
		this.cause = cause;
		this.errorCode = "sysfail";
		this.errorMessage = cause.toString();
	}

	public CommonException(String message) {
		this.errorMessage = message;
	}

//	public CommonException(String expCode, String expDesc, Object objs[]) {
//		this(expCode, expDesc);
//	}

	/**
	 * 增加抛去自己的异常信息，此信息保存在数据库
	 * 
	 * @param errorType
	 * @param errorCode
	 */
	public CommonException(String errorType, String errorCode) {
		super();
		this.errorType = errorType;
		this.errorCode = errorCode;
	}
	
	/**
	 * 增加抛去自己的异常信息，此信息保存在数据库,并有仅有一个占位符
	 * 
	 * @param errorType
	 * @param errorCode
	 */
	public CommonException(String errorType, String errorCode,String param) {
		this(errorType,errorCode,new String[]{param});
	}

	/**
	 * 增加抛去自己的异常信息，此信息保存在数据库,并有多个占位符
	 * @param errorType
	 * @param errorCode
	 * @param params
	 */
	public CommonException(String errorType, String errorCode, String[] params) {
		super();
		this.errorType = errorType;
		this.errorCode = errorCode;
		this.params = params;
	}

	@Override
	public String getMessage() {
		StringBuffer sb = new StringBuffer();
		if (this.cause != null) {
			if ((this.cause instanceof CommonException)) {
				CommonException exception = (CommonException) cause;
				sb.append("原始异常产生原因:\n");
				sb.append(exception.getErrorMessage() + "\n");
			} else {
				sb.append("原始异常产生原因:\n");
				StackTraceElement[] es = this.cause.getStackTrace();
				if (es != null && es.length > 0)
					sb.append(es[0] + "\n");

			}
		}
		sb.append("" + getErrorMessage() + " ");
		return sb.toString();
	}

	@Override
	public Throwable getCause() {
		return cause;
	}

	public void setCause(Throwable cause) {
		this.cause = cause;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}
}
