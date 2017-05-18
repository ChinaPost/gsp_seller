package com.framework.dao.common;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.framework.utils.PaginationBase;

/**
 * @author tang E-mail: killerover84@163.com
 * @version 2016年5月11日 上午10:22:53 类说明 dao 层必要的共公方法类
 */
public class DaoTools {
	private final static String pak = "com.sp.dao.impl";
	
	private final static Random rd = new Random();
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmssSSS");
	

	/**
	 * 获得映射命名空间，当sqlMapId不为空时，返回命名空间 和sql映射id，即sql映射id的全名
	 * 
	 * @param cla
	 * @param sqlMapId
	 * @return
	 */
	public static String getMapperNamespace(Class cla, String sqlMapId) {
		return pak + "." + cla.getSimpleName() + "Mapper" + "." + sqlMapId;
	}

	/**
	 * 生成数据库UUID 主键
	 * 
	 * @return
	 */
	public static String gendUUID() {
		// TODO Auto-generated method stub
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 生成订单号主键
	 * yyMMddHHmmssSSS + 2位业务数据bizData的数字部分 + 随机5位数
	 * @param bizData 业务数据，例如userid
	 */
	public static String genOrderId(String bizData) {
		//当前时间字符串
		Date d = new Date();
		String nowStr = sdf.format(d);
		//五位随机数字符串,,left pad 0
		int nextInt = rd.nextInt(99999);
		String randomStr =  String.valueOf(nextInt) ;
		if(randomStr.length()<5){
			int len = randomStr.length();
			for (int i = 0; i < 5-len; i++) {
				randomStr = "0"+randomStr;
			}
		}
		//2位业务数据bizData的数字部分
		int len = 2;
		String bsDataNum = "";
		for (int i = 0; i < bizData.length(); i++) {
			char c = bizData.charAt(i);
			if(c>=48 && c<=57){
				bsDataNum = bsDataNum+c;
				if(--len <= 0 ){
					break;
				}
			}
		}
		
		return nowStr+bsDataNum+randomStr;
	}
	
	/**
	 * 判断一个对像是所有值是否为空，除id属性外
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNullOrEmpty(Object obj) {
		if (null == obj)
			return true;

		try {
			Class clazz = obj.getClass();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) { // 按定义的字段作为遍历基准
				String fieldName = field.getName();
				if ("id".equals(fieldName))
					continue;
				field.setAccessible( true );
				Object val = field.get(obj);
				if (null != val)
					return false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}

		return true;
	}
	
	
	/**
	 * 建立一个Rltmap
	 * 集成有page信息
	 */
	public static Map<String, Object> genPageRltmap(PaginationBase page) {
        Map<String, Object> rltmap = new HashMap<>();
        if(page!=null){
        	rltmap.put("totalRows", page.getTotalRows());
        	rltmap.put("totalPages", page.getTotalPages());
        	rltmap.put("pageSize", page.getPageSize());
        }
        return rltmap;
	}
	
	/**
	 * 是否重复主键异常
	 */
	public static boolean isDuplicateException(Exception e) {
        return e.getMessage().contains("Duplicate entry");
	}
	
}
