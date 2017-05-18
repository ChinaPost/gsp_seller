package com.framework.pojo;

/** 
* @author tang E-mail: killerover84@163.com
* @version 2016年5月6日 上午10:30:11 
* 类说明  返回JSON串的头部
*/
public class Ret {

	private String retCode;   //错误码，正确时为0000
	private String retError;  //错误信息
	
	public Ret() {
		// TODO Auto-generated constructor stub
	}

	public Ret(String retCode, String retError) {
		this.retCode = retCode;
		this.retError = retError;
	}
	
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	public String getRetError() {
		return retError;
	}
	public void setRetError(String retError) {
		this.retError = retError;
	}

	@Override
	public String toString() {
		return "Ret [retCode=" + retCode + ", retError=" + retError + "]";
	}
}
