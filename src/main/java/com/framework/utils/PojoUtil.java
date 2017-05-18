package com.framework.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;


public class PojoUtil {

	static class ScorePojo {
		private String subject;
		private int score;
		private String[] ids;
		private List<Map> mapList ;
		
		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public int getScore() {
			return score;
		}

		public void setScore(int score) {
			this.score = score;
		}

		public String[] getIds() {
			return ids;
		}

		public void setIds(String[] ids) {
			this.ids = ids;
		}

		public List<Map> getMapList() {
			return mapList;
		}

		public void setMapList(List<Map> mapList) {
			this.mapList = mapList;
		}
	}

	static class User {
		private String username;
		private Integer age;
		private Date birthDay;
		private ScorePojo score;
		private ScorePojo[] scores;
		private Map vo;

		public ScorePojo getScore() {
			return score;
		}

		public void setScore(ScorePojo score) {
			this.score = score;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

		public Date getBirthDay() {
			return birthDay;
		}

		public void setBirthDay(Date birthDay) {
			this.birthDay = birthDay;
		}

		public Map getVo() {
			return vo;
		}

		public void setVo(Map vo) {
			this.vo = vo;
		}

		public ScorePojo[] getScores() {
			return scores;
		}

		public void setScores(ScorePojo[] scores) {
			this.scores = scores;
		}

	}


	//判断是否需要立刻返回的对象
	//例如java基本类型，map等等
	private static boolean isReturnObject(Object obj){
		if(obj instanceof String ||
				obj instanceof Integer ||
				obj instanceof Date ||
				obj instanceof Boolean ||
				obj instanceof Double ||
				obj instanceof Map ||
				obj instanceof Long ||
				obj instanceof Float ||
				obj instanceof Character ||
				obj instanceof Short ||
				obj instanceof Byte
			){
			return true;
		}else{
			return false;
		}
	}
	
	
	
	/**
	 * 让pojo当里的value==null的字段被过滤。减少json的体积，减少网络传输，提高用户体验，节省用户流量。
	 * @param pojo 实体bean
	 * @param needNullFields 值为null的key列表，可以输入多个字段名，即使为null也输出。
	 * @return 单个pojo或者列表pojo
	 * @throws Exception
	 */
	public static Object toMapWithNoNULL(Object pojo , String ... needNullFields)  {
		// TODO Auto-generated method stub
		
		if (pojo == null) {
			return null;
		}
		
		boolean isCollection = false;
		
		List<Object> pojoList = null;
		if(pojo instanceof List){ //本身是集合List
			isCollection=true;
			pojoList = (List)pojo;
		}else if(pojo instanceof Set){ //本身是集合Set
			isCollection=true;
			pojoList = new ArrayList<Object>();
			pojoList.addAll( (Set)pojo );
		}else if(pojo instanceof Object[]){ //本身是数组
			isCollection=true;
			pojoList = Arrays.asList((Object[])pojo);
		}else{
			
			//普通对象，有且只有1个元素
			pojoList = new ArrayList<Object>();
			pojoList.add(pojo); //不管如何先看做是一个列表。
		}
		
		List<Object> retList = new ArrayList<Object>();
		for (Object pojoItem : pojoList) {
			
			if(isReturnObject(pojoItem)){
				//基本类型
				retList.add(pojoItem);
				continue; //返回
			}
			
			//如果是对象类型
			Map<String, Object> tempMap = new HashMap<String, Object>();
			Map<String, Object> sortedMap = new LinkedHashMap<String, Object>();

			//内省===
			try {
				BeanInfo beanInfo = Introspector.getBeanInfo(pojoItem.getClass());
				PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
				for (PropertyDescriptor property : propertyDescriptors) {
					String key = property.getName();

					// 过滤class属性
					if (!key.equals("class")) {
						// 得到property对应的getter方法
						Method getter = property.getReadMethod();
						Object value = getter.invoke(pojoItem);
						
						//过滤
						if(value==null ){
							if(  ArrayUtils.contains(needNullFields, key) ){//字段名，在排除列表里，也就是放行

							}else{
								continue;
							}
						}

						if(isReturnObject(value)){
							//基本类型
							tempMap.put(key, value);
						}else{
							tempMap.put(key, toMapWithNoNULL(value,needNullFields));
						}
					}
				}//for propertyDescriptors
			} catch (IllegalAccessException |IllegalArgumentException | InvocationTargetException | IntrospectionException e) {
				e.printStackTrace();
			}
			
			
			/**
			 * 按原本pojo顺序排序
			 * */
			Field[] fields = pojoItem.getClass().getDeclaredFields();
			for (Field field : fields) {
				if(tempMap.containsKey(field.getName())){
					sortedMap.put(field.getName(), tempMap.get(field.getName()));
				}
			}
			tempMap.clear();
			/****/
			
			
			retList.add(sortedMap);
		}//for pojoList
		
		return isCollection ? retList : retList.get(0);
	}


	/*
	 * 只有包含的字段允许输出，否则会被排除。
	 * @param srcObject map或mapList对象
	 * @param allowFields 允许列表，可以输入多个字段名
	 * @return 单个pojo或者列表pojo 元素类型是map
	 * @throws Exception
	 */
	private static Object filterMapWithAllow(Object srcObject , String ... allowFields)  {
		if (srcObject == null) {
			return null;
		}
		
		boolean isCollection = false;
		
		List<Object> pojoMapList = null;
		if(srcObject instanceof List){ //本身是集合List
			isCollection=true;
			pojoMapList = (List)srcObject;
		}else if(srcObject instanceof Set){ //本身是集合Set
			isCollection=true;
			pojoMapList = new ArrayList<Object>();
			pojoMapList.addAll( (Set)srcObject );
		}else if(srcObject instanceof Object[]){ //本身是数组
			isCollection=true;
			pojoMapList = Arrays.asList((Object[])srcObject);
		}else{
			
			//普通对象，有且只有1个元素
			pojoMapList = new ArrayList<Object>();
			pojoMapList.add(srcObject); //不管如何先看做是一个列表。
		}
		
		List<Object> retList = new ArrayList<Object>();
		for (Object item : pojoMapList) {
			Map pojoMapItem = (Map) item;
			
			Map mapTemp = new LinkedHashMap<>();
			Set<Entry> entrySet = pojoMapItem.entrySet();
			for (Entry entry : entrySet) {
				if (ArrayUtils.contains(allowFields, entry.getKey())) {//允许列表 包含有
					if(entry.getValue() instanceof Map){
						mapTemp.put(entry.getKey(), filterMapWithAllow(entry.getValue(),allowFields));
					}else{
						mapTemp.put(entry.getKey(),entry.getValue());
					}
				}
			}//for
			
			retList.add(mapTemp);
		}//for
		
		
		return isCollection ? retList : retList.get(0);
	}
	
	
	
	/**
	 * 只有包含的字段允许输出，否则会被排除。
	 * @param pojo 实体bean
	 * @param allowFields 允许列表，可以输入多个字段名
	 * @return 单个pojo或者列表pojo 元素类型是map
	 * @throws Exception
	 */
	public static Object toMapWithAllow(Object pojo , String ... allowFields)  {
		Object mapWithNoNULL = toMapWithNoNULL(pojo,allowFields);
		return filterMapWithAllow(mapWithNoNULL,allowFields);
	}
	
	
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		User u1 = new User();
		u1.setBirthDay(new Date());
		u1.setUsername("gz");
		u1.setAge(123);
		u1.setVo(new HashMap(){{put(1,1);put(2,2);}});
		
		ScorePojo score = new ScorePojo();
		score.setScore(100);
		score.setSubject("语文");
		score.setIds(new String[]{"123","122","133"});
		
		{
			List<Map> mapList = new ArrayList();
			for (int i = 0; i < 3; i++) {
				Map m = new HashMap();
				m.put("1"+i , "100");
				mapList.add(m);
			}
			score.setMapList(mapList);
		}
		
		u1.setScore(score);
		u1.setScores(new ScorePojo[]{ new ScorePojo(){{setScore(12);setSubject("数学");}} ,  new ScorePojo(){{setScore(15);setSubject("英语");}} });
		
		Object pojo2Map = PojoUtil.toMapWithNoNULL(u1,"aa","bb");
		System.out.println(pojo2Map );
		
		Object allow = PojoUtil.toMapWithAllow(pojo2Map,"age","score","subject");
		System.out.println(allow );
		
	}

}
