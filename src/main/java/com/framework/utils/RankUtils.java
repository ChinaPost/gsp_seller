package com.framework.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 排名计算工具类
 */
public class RankUtils {

	/**
	 * 计算排名，成功返回排名数，失败返回-1
	 * @param list double列表，可无序。
	 * @param value 参与排名的double值
	 * @param maxIsNo1 true为最大排第一，false相反
	 * @return 名次
	 */
	public static int rank(List<Double> list, Double value , boolean maxIsNo1) {
		
		if (list == null || list.isEmpty()) //如果没有列表，不进行排名
			return -1;
		
		List<Double> temp = new ArrayList<Double>(list);
		
		Collections.sort(temp); // 升序

		int rank = 0;
		if(maxIsNo1){ //第一名最大
			for( int i=temp.size()-1 ; i>=0; i-- ){
				rank++;
				if(temp.get(i).equals(value)){
					break;
				}
				if(i==0) rank=-1;//没有匹配
			}
		}else{//第一名最小
			for( int i=0 ; i<temp.size(); i++ ){
				rank++;
				if(temp.get(i).equals(value)){
					break;
				}
				if(i==temp.size()-1) rank=-1;//没有匹配
			}
		}
		
		
		temp.clear();//清理
		
		return rank;
	}
	
	
	public static void main(String[] args) {
		//测试
		List list = new ArrayList();
		list.add(3.3);
		list.add(2.3);
		list.add(4.3);
		list.add(5.3);
		
		int rank = RankUtils.rank(list, 4.3 , true);
		System.out.println(list);
		System.out.println(rank);
	}
	
	
	
}
