package com.cpz.utils;

/**
 * @author tang E-mail: killerover84@163.com
 * @version 2016年5月9日 下午1:40:57 类说明 常量放此
 */
public class Constant {
	// 错误类型
	public final static String ERRORTYPE = "01";
	// ===============================下面是错误信息 start========================
	// 系统层错误码
	public final static String TRAN_UNKONW = "9999";// 系统未知错误
	public final static String TRAN_SYSERR = "9998";// 系统正在维护中
	public final static String TRAN_BADWORD = "9997";// 输入包含敏感词

	// 数据库层错误码
	public final static String TRAN_DBERR = "9996";// 操作数据库失败
	public final static String TRAN_DBNULL = "9995";// 读取数据库数据为空

	// 交易错误码
	public final static String TRAN_SUCCESS = "0000";// 交易成功

	public final static String TRAN_COMMONEXCEP = "9994";// 异常错误码
	public final static String TRAN_PARAERCODE = "9993";// 请求参数错误
	// =================
	public static final String TRAN_UPLOADPARAERROR = "9992";// 上传参数错误
	public static final String TRAN_FILEISNULL = "9991";// 上传文件为空
	public static final String TRAN_UNUPLOAD = "9990";// 禁止上传文件
	public static final String TRAN_UNIMG = "9989";// 禁止上传此类文件
	public static final String TRAN_LIMIT = "9988";// 上传文件大小超出限制
	public static final String TRAN_ALLOW = "9987";// 仅允许上传格式为：{0}文件

	
	
	
	
	// ,分格符
	public static final String EXTENSION_SEPARATOR = ",";
}
