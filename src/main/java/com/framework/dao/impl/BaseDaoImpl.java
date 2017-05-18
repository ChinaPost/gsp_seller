package com.framework.dao.impl;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.framework.condition.Condition;
import com.framework.dao.BaseDao;
import com.framework.dao.common.CountHelper;
import com.framework.dao.common.DaoTools;
import com.framework.exception.CommonException;
import com.framework.utils.PaginationBase;

/**
 * 
 * @author tang
 * @version 2016-04-18 持久化基础类的实现 1、以后所有的dao都可以继承之类
 *          2、建议直接用些类，不要写都dao层，只有在此类做不的业务是可以增加dao层
 */
@Component("baseDao")
public class BaseDaoImpl extends SqlSessionDaoSupport implements BaseDao {
	private final static Logger logger = LoggerFactory.getLogger(BaseDaoImpl.class);

	@Override
	@Autowired
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}

	

	/**
	 * 通过反射 补全 默认的id,createDate,modifyDate,updateDate字段
	 * @param isUpdate 是否更新
	 */
	private void completeDefaultField(Object objParam , boolean isUpdate) {
		// TODO Auto-generated method stub
		
		Field field_id = FieldUtils.getField(objParam.getClass(), "id", true);
		Field field_createDate = FieldUtils.getField(objParam.getClass(), "createDate", true);
		Field field_modifyDate = FieldUtils.getField(objParam.getClass(), "modifyDate", true);
		Field field_updateDate = FieldUtils.getField(objParam.getClass(), "updateDate", true);
		
		try {
			if( isUpdate==false && field_id!=null){ //insert才要
				Object id = FieldUtils.readField(objParam, "id", true);
				if(id==null){
					FieldUtils.writeField(objParam ,"id" , DaoTools.gendUUID(), true);
				}
			}
			if( isUpdate==false && field_createDate!=null ){ //insert才要
				Object createDate = FieldUtils.readField(objParam, "createDate", true);
				if(createDate==null){
					FieldUtils.writeField(objParam ,"createDate" , new Date() , true);
				}
			}
			
			if(field_modifyDate!=null){
				Object modifyDate = FieldUtils.readField(objParam, "modifyDate", true);
				if(modifyDate==null){
					FieldUtils.writeField(objParam ,"modifyDate" , new Date()  , true);
				}
			}
				
			if(field_updateDate!=null){
				Object updateDate = FieldUtils.readField(objParam, "updateDate", true);
				if(updateDate==null){
					FieldUtils.writeField(objParam ,"updateDate" , new Date() , true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据sqlMapid，保存数据,只保存对象
	 * 可以自动补全常用的id,createDate,modifyDate,updateDate字段
	 * 
	 * @param sqlMapId
	 * @param obj
	 * @return
	 */
	@Override
	public int insert(String sqlMapId, Object objParam) throws CommonException {
		logger.debug(" delete [sqlMapId=" + sqlMapId + "] parem==>" + (objParam == null ? null : objParam.toString()));

		//设置默认的id,createDate,modifyDate,updateDate
		completeDefaultField(objParam,false);
		
		// 指定命名空间
		sqlMapId = DaoTools.getMapperNamespace(objParam.getClass(), sqlMapId);

		long startTime = System.currentTimeMillis();
		try {
			int num = this.getSqlSession().insert(sqlMapId, objParam);

			if ((System.currentTimeMillis() - startTime) > 5000) {
				logger.info(
						"  insert [" + sqlMapId + "]  end " + "耗时=" + (System.currentTimeMillis() - startTime) + " ms");
			}

			return num;

		} catch (Exception e) {
			throw new CommonException(e);
		}
	}


	
	
	/**
	 * 根据sqlMapid，保存数据 ,参数只能是MAP
	 * 
	 * @param sqlMapId
	 * @param obj
	 * @return
	 */
	@Override
	public int insert(String sqlMapId, Map<String, Object> mapParam) throws CommonException {
		logger.debug(" insert [sqlMapId=" + sqlMapId + "] parem==>" + (mapParam == null ? null : mapParam.toString()));

		long startTime = System.currentTimeMillis();

		try {
			int num = this.getSqlSession().insert(sqlMapId, mapParam);

			if ((System.currentTimeMillis() - startTime) > 5000) {
				logger.info(
						"  insert [" + sqlMapId + "]  end " + "耗时=" + (System.currentTimeMillis() - startTime) + " ms");
			}

			return num;

		} catch (Exception e) {
			throw new CommonException(e);
		}
	}

	/**
	 * 根据sqlMapid，保存数据,参数本系统指定参数对象
	 * 
	 * @param sqlMapId
	 * @param obj
	 * @return
	 */
	@Override
	public int insert(String sqlMapId, Condition cond) throws CommonException {

		long startTime = System.currentTimeMillis();

		try {

			if (cond == null) {
				logger.debug("未带任何条件查询信息... sqlMapId=" + sqlMapId);
			}

			Map<String, Object> mapParam = new HashMap<String, Object>();

			if (cond != null)
				mapParam = cond.getCondMap();

			logger.debug(
					" insert [sqlMapId=" + sqlMapId + "] parem==>" + (mapParam == null ? null : mapParam.toString()));

			int num = this.getSqlSession().insert(sqlMapId, mapParam);

			if ((System.currentTimeMillis() - startTime) > 5000) {
				logger.info(
						"  insert [" + sqlMapId + "]  end " + "耗时=" + (System.currentTimeMillis() - startTime) + " ms");
			}

			return num;

		} catch (Exception e) {
			throw new CommonException(e);
		}
	}

	/**
	 * 根据sqlMapid，更新数据 ,参数只能是对像
	 * 可以自动补全常用的id,modifyDate,updateDate字段
	 * 
	 * @param sqlMapId
	 * @param obj
	 * @return
	 */
	@Override
	public int update(String sqlMapId, Object objParam) throws CommonException {
		logger.debug(" delete [sqlMapId=" + sqlMapId + "] parem==>" + (objParam == null ? null : objParam.toString()));

		//设置默认的modifyDate,updateDate
		completeDefaultField(objParam,true);
		
		// 指定命名空间
		sqlMapId = DaoTools.getMapperNamespace(objParam.getClass(), sqlMapId);

		long startTime = System.currentTimeMillis();
		int returnCount = 0;
		try {

			returnCount = this.getSqlSession().update(sqlMapId, objParam);
			if ((System.currentTimeMillis() - startTime) > 5000) {
				logger.info(
						"  update [" + sqlMapId + "]  end " + "耗时=" + (System.currentTimeMillis() - startTime) + " ms");
			}

			return returnCount;

		} catch (Exception e) {
			throw new CommonException(e);
		}
	}

	/**
	 * 根据sqlMapid，更新数据 ,参数只能是Map
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	@Override
	public int update(String sqlMapId, Map<String, Object> mapParam) throws CommonException {
		logger.debug(" delete [sqlMapId=" + sqlMapId + "] parem==>" + (mapParam == null ? null : mapParam.toString()));

		int returnCount = 0;
		long startTime = System.currentTimeMillis();
		try {

			returnCount = this.getSqlSession().update(sqlMapId, mapParam);
			if ((System.currentTimeMillis() - startTime) > 5000) {
				logger.info(
						"  update [" + sqlMapId + "]  end " + "耗时=" + (System.currentTimeMillis() - startTime) + " ms");
			}

			return returnCount;

		} catch (Exception e) {
			throw new CommonException(e);
		}
	}

	/**
	 * 根据参数condition里面的参数，更新数据
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	@Override
	public int update(String sqlMapId, Condition cond) throws CommonException {

		long startTime = System.currentTimeMillis();
		try {
			if (cond == null) {
				logger.debug("未带任何条件查询信息... sqlMapId=" + sqlMapId);
			}

			Map<String, Object> mapParam = new HashMap<String, Object>();

			if (cond != null)
				mapParam = cond.getCondMap();

			logger.debug(
					" update [sqlMapId=" + sqlMapId + "] parem==>" + (mapParam == null ? null : mapParam.toString()));

			return this.getSqlSession().update(sqlMapId, mapParam);

		} catch (Exception e) {
			throw new CommonException(e);
		} finally {
			if ((System.currentTimeMillis() - startTime) > 5000)
				logger.info(" update [" + sqlMapId + "]  查询end " + "耗时=" + (System.currentTimeMillis() - startTime)
						+ " ms");

		}
	}

	/**
	 * 根据sqlMapid，删除对像 参数仅对象
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	@Override
	public int delete(String sqlMapId, Object objParam) throws CommonException {
		logger.debug(" delete [sqlMapId=" + sqlMapId + "] parem==>" + (objParam == null ? null : objParam.toString()));

		// 指定命名空间
		sqlMapId = DaoTools.getMapperNamespace(objParam.getClass(), sqlMapId);

		long startTime = System.currentTimeMillis();
		int returnCount = 0;

		try {

			returnCount = this.getSqlSession().delete(sqlMapId, objParam);

			if ((System.currentTimeMillis() - startTime) > 5000) {
				logger.info(
						"  delete [" + sqlMapId + "]  end " + "耗时=" + (System.currentTimeMillis() - startTime) + " ms");
			}

			return returnCount;

		} catch (Exception e) {
			throw new CommonException(e);
		}
	}

	/**
	 * 根据sqlMapid，删除对像 参数仅Map
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	@Override
	public int delete(String sqlMapId, Map<String, Object> mapParam) throws CommonException {
		logger.debug(" delete [sqlMapId=" + sqlMapId + "] parem==>" + (mapParam == null ? null : mapParam.toString()));

		long startTime = System.currentTimeMillis();
		int returnCount = 0;
		try {

			returnCount = this.getSqlSession().delete(sqlMapId, mapParam);

			if ((System.currentTimeMillis() - startTime) > 5000) {
				logger.info(
						"  delete [" + sqlMapId + "]  end " + "耗时=" + (System.currentTimeMillis() - startTime) + " ms");
			}

			return returnCount;
		} catch (Exception e) {
			throw new CommonException(e);
		}
	}

	/**
	 * 根据sqlMapid，批量删除，其中参数是主键集合
	 */
	@Override
	public int delete(String sqlMapId, List reqList) throws CommonException {
		// TODO Auto-generated method stub
		logger.debug(" delete [sqlMapId=" + sqlMapId + "] parem==>" + (reqList == null ? null : reqList.toString()));

		long startTime = System.currentTimeMillis();
		int returnCount = 0;
		try {
			returnCount = this.getSqlSession().delete(sqlMapId, reqList);
			if ((System.currentTimeMillis() - startTime) > 5000) {
				logger.info(
						"  delete [" + sqlMapId + "]  end " + "耗时=" + (System.currentTimeMillis() - startTime) + " ms");
			}
			return returnCount;
		} catch (Exception e) {
			throw new CommonException(e);
		}
	}
	
	/**
	 * 根据数组批量查询
	 */
	@Override
	public List selectAll(String sqlMapId, List reqList) throws CommonException {
		// TODO Auto-generated method stub
		logger.debug(" selectAll [sqlMapId=" + sqlMapId + "] parem==>" + (reqList == null ? null : reqList.toString()));

		long startTime = System.currentTimeMillis();
		try {
			return this.getSqlSession().selectList(sqlMapId, reqList);
		} catch (Exception e) {
			throw new CommonException(e);
		} finally {
			if ((System.currentTimeMillis() - startTime) > 5000)
				logger.info("  selectAll [" + sqlMapId + "]  end " + "耗时=" + (System.currentTimeMillis() - startTime)
						+ " ms");
		}
		
	}

	/**
	 * 根据条件condition，查询全部的记录，返回volist
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	@Override
	public List selectAll(String sqlMapId) throws CommonException {
		long startTime = System.currentTimeMillis();
		try {
			Map<String, Object> mapParam = new HashMap<String, Object>();
			return this.getSqlSession().selectList(sqlMapId, mapParam);
		} catch (Exception e) {
			throw new CommonException(e);
		} finally {
			if ((System.currentTimeMillis() - startTime) > 5000)
				logger.info("  selectAll [" + sqlMapId + "]  end " + "耗时=" + (System.currentTimeMillis() - startTime)
						+ " ms");
		}
	}

	/**
	 * 根据条件condition，查询全部的记录，返回volist
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	@Override
	public List selectAll(String sqlMapId, Condition cond) throws CommonException {
		long startTime = System.currentTimeMillis();
		try {
			if (cond == null) {
				logger.debug("未带任何条件查询信息... sqlMapId=" + sqlMapId);
			}

			Map<String, Object> mapParam = new HashMap<String, Object>();

			if (cond != null)
				mapParam = cond.getCondMap();

			logger.debug(" selectAll [sqlMapId=" + sqlMapId + "] parem==>"
					+ (mapParam == null ? null : mapParam.toString()));

			return this.getSqlSession().selectList(sqlMapId, mapParam);

		} catch (Exception e) {
			throw new CommonException(e);
		} finally {
			if ((System.currentTimeMillis() - startTime) > 5000)
				logger.info("  selectAll [" + sqlMapId + "]  end " + "耗时=" + (System.currentTimeMillis() - startTime)
						+ " ms");
		}
	}

	/**
	 * 根据条件集，查询全部的记录，返回volist
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	@Override
	public List selectAll(String sqlMapId, Map<String, Object> mapParam) throws CommonException {
		long startTime = System.currentTimeMillis();
		try {
			if (mapParam == null || mapParam.isEmpty()) {
				logger.debug("未带任何条件查询信息... sqlMapId=" + sqlMapId);
			} else {
				logger.debug(" selectAll [sqlMapId=" + sqlMapId + "] parem==>"
						+ (mapParam == null ? null : mapParam.toString()));
			}

			return this.getSqlSession().selectList(sqlMapId, mapParam);

		} catch (Exception e) {
			throw new CommonException(e);
		} finally {
			if ((System.currentTimeMillis() - startTime) > 5000)
				logger.info(" selectAll [" + sqlMapId + "]  查询end " + "耗时=" + (System.currentTimeMillis() - startTime)
						+ " ms");

		}
	}
	
	/**
	 * 根据条件mapParam,page，分页查询记录，返回volist
	 * 若page==null，则查询全部
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	@Override
	public List selectPage(String sqlMapId, Map<?, ?> mapParam , PaginationBase page) throws CommonException { 
		try {
			
			if(page==null || page.getPageNo()==0){//不带分页信息
				return this.selectAll(sqlMapId, (Map<String, Object>)mapParam);
			}
			
			long startTime = System.currentTimeMillis();
			RowBounds rowBounds = new RowBounds((int) page.getPageStartRow(), page.getPageSize());
			
			logger.debug(" selectPage [sqlMapId=" + sqlMapId + "] parem==>"
					+ (mapParam == null ? null : mapParam.toString()));
			
			List resultList = this.getSqlSession().selectList(sqlMapId, mapParam, rowBounds);
			
			if ((System.currentTimeMillis() - startTime) > 5000)
				logger.info(" selectPage [" + sqlMapId + "] 翻页查询end " + "耗时=" + (System.currentTimeMillis() - startTime)
						+ " ms");
			
			// 设置总记录数
			page.calculate(CountHelper.getTotalRowCount());
			
			return resultList;
		} catch (Exception e) {
			throw new CommonException(e);
		}
	}

	/**
	 * 根据条件condition，分页查询记录，返回volist
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	@Override
	public List selectPage(String sqlMapId, Condition cond) throws CommonException { 
		try {
			
//			if(cond.getPage()==null || cond.getPage().getPageSize()>=9999999){//不带分页信息
//				return this.selectAll(sqlMapId, cond);
//			}
			
			if (cond == null || cond.getCondMap().size() == 0) {
				logger.debug("未带任何条件查询信息... sqlMapId=" + sqlMapId);
			}
			long startTime = System.currentTimeMillis();
			RowBounds rowBounds = new RowBounds((int) cond.getPage().getPageStartRow(), cond.getPage().getPageSize());
			Map<String, Object> mapParam = new HashMap<String, Object>();
			if (cond != null)
				mapParam = cond.getCondMap();

			logger.debug(" selectPage [sqlMapId=" + sqlMapId + "] parem==>"
					+ (mapParam == null ? null : mapParam.toString()));

			List resultList = this.getSqlSession().selectList(sqlMapId, mapParam, rowBounds);

			if ((System.currentTimeMillis() - startTime) > 5000)
				logger.info(" selectPage [" + sqlMapId + "] 翻页查询end " + "耗时=" + (System.currentTimeMillis() - startTime)
						+ " ms");

			// 设置总记录数
			cond.getPage().calculate(CountHelper.getTotalRowCount());

			return resultList;
		} catch (Exception e) {
			throw new CommonException(e);
		}
	}

	/**
	 * 根据条件查询单一对象
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 * @throws CommonException
	 */
	@Override
	public Object selectOne(String sqlMapId, Map<String, Object> mapParam) throws CommonException {
		try {
			logger.debug(" selectOne [sqlMapId=" + sqlMapId + "] parem==>"
					+ (mapParam == null ? null : mapParam.toString()));

			long startTime = System.currentTimeMillis();

			Object obj = this.getSqlSession().selectOne(sqlMapId, mapParam);

			if ((System.currentTimeMillis() - startTime) > 5000)
				logger.info("selectOne [" + sqlMapId + "] " + "耗时=" + (System.currentTimeMillis() - startTime) + " ms");

			return obj;

		} catch (Exception e) {
			throw new CommonException(e);
		}
	}

	/**
	 * 查询单一对象
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 * @throws CommonException
	 */
	@Override
	public Object selectOne(String sqlMapId) throws CommonException {
		try {
			long startTime = System.currentTimeMillis();

			Object obj = this.getSqlSession().selectOne(sqlMapId);

			if ((System.currentTimeMillis() - startTime) > 5000)
				logger.info("selectOne [" + sqlMapId + "] " + "耗时=" + (System.currentTimeMillis() - startTime) + " ms");

			return obj;

		} catch (Exception e) {
			throw new CommonException(e);
		}
	}

	/**
	 * 根据条件condition（其中封装了主键key条件），查询单条记录，返回vo对象
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Object selectByKey(String sqlMapId, Condition cond) throws CommonException {
		long startTime = System.currentTimeMillis();
		try {

			if (cond == null || cond.getCondMap().size() == 0) {
				logger.debug(sqlMapId + "未带任何条件查询,则不执行查询操作，直接返回null...");
				throw new CommonException("查询值不能为空，请先输入条件，然后再查询！！！！查询信息sqlMapId=" + sqlMapId);

			} else {

				Map<String, Object> mapParam = new HashMap<String, Object>();
				if (cond != null)
					mapParam = cond.getCondMap();

				logger.debug(" selectByKey [sqlMapId=" + sqlMapId + "] parem==>"
						+ (mapParam == null ? null : mapParam.toString()));

				List<Object> objList = this.getSqlSession().selectList(sqlMapId, mapParam);
				if (objList != null && objList.size() > 0)
					return objList.get(0);
				else
					return null;
			}
		} catch (Exception e) {
			throw new CommonException(e);
		} finally {
			if ((System.currentTimeMillis() - startTime) > 5000)
				logger.info(" selectByKey [" + sqlMapId + "] 查询end " + "耗时=" + (System.currentTimeMillis() - startTime)
						+ " ms");

		}

	}

	/**
	 * 根据条件condition（其中封装了主键key条件），查询单条记录，返回vo对象
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Object selectByKey(String sqlMapId, Map<String, Object> mapParam) throws CommonException {
		long startTime = System.currentTimeMillis();
		try {

			if (mapParam == null || mapParam.size() == 0) {
				logger.info(sqlMapId + "未带任何条件查询,则不执行查询操作，直接返回null...");
				throw new CommonException("查询值不能为空，请先输入条件，然后再查询！！！！查询信息sqlMapId=" + sqlMapId);
			} else {
				logger.debug(" selectByKey [sqlMapId=" + sqlMapId + "] parem==>"
						+ (mapParam == null ? null : mapParam.toString()));

				List<Object> objList = this.getSqlSession().selectList(sqlMapId, mapParam);
				if (objList != null && objList.size() > 0)
					return objList.get(0);
				else
					return null;
			}
		} catch (Exception e) {
			throw new CommonException(e);
		} finally {
			if ((System.currentTimeMillis() - startTime) > 5000)
				logger.info(" selectByKey [" + sqlMapId + "] 查询end " + "耗时=" + (System.currentTimeMillis() - startTime)
						+ " ms");

		}

	}

	/**
	 * 调用存储过程,执行需要返回值
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	@Override
	public Object callProcedureBySqlMapId(String sqlMapId, Condition cond) throws CommonException {
		long startTime = System.currentTimeMillis();
		try {
			Map<String, Object> mapParam = new HashMap<String, Object>();
			if (cond != null)
				mapParam = cond.getCondMap();
			this.getSqlSession().selectOne(sqlMapId, mapParam);
			return mapParam;
		} catch (Exception e) {
			throw new CommonException(e);
		} finally {
			if ((System.currentTimeMillis() - startTime) > 5000)
				logger.info(" callProcedureBySqlMapId [" + sqlMapId + "] 查询end " + "耗时="
						+ (System.currentTimeMillis() - startTime) + " ms");

		}
	}

	/**
	 * 根据参数condition里面的参数，执行指定的sql
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	@Override
	public int executeBySqlMapId(String sqlMapId, Condition cond) throws CommonException {
		long startTime = System.currentTimeMillis();
		try {
			Map<String, Object> mapParam = new HashMap<String, Object>();
			if (cond != null)
				mapParam = cond.getCondMap();
			return this.getSqlSession().update(sqlMapId, mapParam);
		} catch (Exception e) {
			throw new CommonException(e);
		} finally {
			if ((System.currentTimeMillis() - startTime) > 5000)
				logger.info(" executeBySqlMapId [" + sqlMapId + "] 查询end " + "耗时="
						+ (System.currentTimeMillis() - startTime) + " ms");

		}
	}

	/**
	 * 执行指定的sql，获取第一行的数据
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	@Override
	public Object selectFirstRowBySqlMapId(String sqlMapId, Condition cond) {
		long startTime = System.currentTimeMillis();
		try {
			if (cond == null || cond.getCondMap().size() == 0) {
				logger.info("未带任何条件查询信息... sqlMapId=" + sqlMapId);
			}
			if (("TB_SM_MACHINEINFO_select".equals(sqlMapId) || "TB_SM_MACHINEINFO_selectByKey".equals(sqlMapId))
					&& (cond == null || cond.getCondMap().size() == 0)) {
				logger.info("TB_SM_MACHINEINFO_selectFirstRowBySqlMapId未带任何条件查询机器信息,则不执行查询操作，直接返回null...");

				return null;
			} else {

				Map<String, Object> mapParam = new HashMap<String, Object>();
				if (cond != null)
					mapParam = cond.getCondMap();
				List lstResult = this.getSqlSession().selectList(sqlMapId, mapParam);
				if (null != lstResult && !lstResult.isEmpty())
					return lstResult.get(0);
				else
					return null;
			}
		} catch (Exception e) {
			// throw new CommonException(e);
			e.printStackTrace();
			logger.error(e.toString());
			return null;
		} finally {
			if ((System.currentTimeMillis() - startTime) > 5000)
				logger.info(" selectFirstRowBySqlMapId [" + sqlMapId + "] 查询end " + "耗时="
						+ (System.currentTimeMillis() - startTime) + " ms");

		}

	}

	/**
	 * 执行指定的sql，获取最后一行的数据
	 * 
	 * @param sqlMapId
	 * @param cond
	 * @return
	 */
	@Override
	public Object selectMaxRowBySqlMapId(String sqlMapId, Condition cond) {
		long startTime = System.currentTimeMillis();
		try {
			if (cond == null || cond.getCondMap().size() == 0) {
				logger.info("未带任何条件查询信息... sqlMapId=" + sqlMapId);
			}
			Map<String, Object> mapParam = new HashMap<String, Object>();
			if (cond != null)
				mapParam = cond.getCondMap();
			List lstResult = this.getSqlSession().selectList(sqlMapId, mapParam);
			if (null != lstResult && !lstResult.isEmpty())
				return lstResult.get(lstResult.size() - 1);
			else
				return null;
		} catch (Exception e) {
			// throw new CommonException(e);
			e.printStackTrace();
			logger.error(e.toString());
			return null;
		} finally {
			if ((System.currentTimeMillis() - startTime) > 5000)
				logger.info(" selectMaxRowBySqlMapId [" + sqlMapId + "] 查询end " + "耗时="
						+ (System.currentTimeMillis() - startTime) + " ms");

		}

	}

}
