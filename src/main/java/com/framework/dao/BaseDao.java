package com.framework.dao;

import java.util.Map;
import java.util.List;

import com.framework.condition.Condition;
import com.framework.exception.CommonException;
import com.framework.utils.PaginationBase;

/**
 * 
 * @author tang
 * @version 2016-04-18 说明：基础DAO类，封装了持久化的基本CUID方法 1、以后所有的dao都可以继承之类
 *          2、建议直接用些类，不要写都dao层，只有在此类做不的业务是可以增加dao层
 */
public interface BaseDao {
	/**
	 * 根据sqlMapid，保存数据,只保存对象
	 * 
	 * @param sqlMapId
	 * @param obj
	 * @return
	 */
	public int insert(String sqlMapId, Object objParam) throws CommonException;

	/**
	 * 根据sqlMapid，保存数据 ,参数只能是MAP
	 * 
	 * @param sqlMapId
	 * @param obj
	 * @return
	 */
	public int insert(String sqlMapId, Map<String, Object> mapParam) throws CommonException;

	/**
	 * 根据sqlMapid，保存数据,参数本系统指定参数对象
	 * 
	 * @param sqlMapId
	 * @param obj
	 * @return
	 */
	public int insert(String sqlMapId, Condition cond) throws CommonException;

	/**
	 * 根据sqlMapid，更新数据 ,参数只能是对像
	 * 
	 * @param sqlMapId
	 * @param obj
	 * @return
	 */
	public int update(String sqlMapId, Object objParam) throws CommonException;

	/**
	 * 根据sqlMapid，更新数据 ,参数只能是Map
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	public int update(String sqlMapId, Map<String, Object> mapParam) throws CommonException;

	/**
	 * 根据参数condition里面的参数，更新数据
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	public int update(String sqlMapId, Condition cond) throws CommonException;

	/**
	 * 根据sqlMapid，删除对像 参数仅对象
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	public int delete(String sqlMapId, Object objParam) throws CommonException;

	/**
	 * 根据sqlMapid，删除对像 参数仅Map
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	public int delete(String sqlMapId, Map<String, Object> mapParam) throws CommonException;

	/**
	 * 根据sqlMapid，批量删除，其中参数是主键集合
	 */
	public int delete(String sqlMapId, List reqList) throws CommonException;

	/**
	 * 根据id数组批量查询
	 * @param sqlMapId
	 * @param reqList
	 * @return List
	 * @throws CommonException
	 */
	public List selectAll(String sqlMapId, List reqList) throws CommonException;
	
	/**
	 * 根据条件condition，查询全部的记录，返回volist
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	public List selectAll(String sqlMapId) throws CommonException;

	/**
	 * 根据条件condition，查询全部的记录，返回volist
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	public List selectAll(String sqlMapId, Condition cond) throws CommonException;

	/**
	 * 根据条件集，查询全部的记录，返回volist
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	public List selectAll(String sqlMapId, Map<String, Object> mapParam) throws CommonException;

	/**
	 * 根据条件condition，分页查询记录，返回volist
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	public List selectPage(String sqlMapId, Map<?, ?> mapParam , PaginationBase page) throws CommonException;
	
	/**
	 * 根据条件condition，分页查询记录，返回volist
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	public List selectPage(String sqlMapId, Condition cond) throws CommonException;

	/**
	 * 根据条件查询单一对象
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 * @throws CommonException
	 */
	public Object selectOne(String sqlMapId, Map<String, Object> mapParam) throws CommonException;

	/**
	 * 查询单一对象
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 * @throws CommonException
	 */
	public Object selectOne(String sqlMapId) throws CommonException;

	/**
	 * 根据条件condition（其中封装了主键key条件），查询单条记录，返回vo对象
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object selectByKey(String sqlMapId, Condition cond) throws CommonException;

	/**
	 * 根据条件condition（其中封装了主键key条件），查询单条记录，返回vo对象
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object selectByKey(String sqlMapId, Map<String, Object> mapParam) throws CommonException;

	/**
	 * 调用存储过程,执行后不需要返回值
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	public Object callProcedureBySqlMapId(String sqlMapId, Condition cond) throws CommonException;

	/**
	 * 根据参数condition里面的参数，执行指定的sql
	 * 
	 * @param SqlMapId
	 * @param cond
	 * @return
	 */
	public int executeBySqlMapId(String SqlMapId, Condition cond) throws CommonException;

	/**
	 * 执行指定的sql，获取第一行的数据
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	public Object selectFirstRowBySqlMapId(String sqlMapId, Condition cond);

	/**
	 * 执行指定的sql，获取最后一行的数据
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	public Object selectMaxRowBySqlMapId(String sqlMapId, Condition cond);

}
