package com.cpz.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.framework.dao.BaseDao;
import com.framework.dao.common.DaoTools;
import com.framework.exception.CommonException;
import com.cpz.pojo.ErrorInfo;
import com.cpz.service.ErrorService;

/** 
* @author tang E-mail: killerover84@163.com
* @version 2016年5月9日 下午1:49:20 
* 类说明 
*/
@Service("errorService")
public class ErrorServiceImpl implements ErrorService {
	private final static Logger logger = LoggerFactory.getLogger(ErrorServiceImpl.class);
	
	@Resource
	private BaseDao baseDao;
	
	@Override
	public ErrorInfo getErrorInfo(String errorCode, String errorType) throws CommonException {
		// TODO Auto-generated method stub
		Map<String,Object> m = new HashMap<>();
		m.put("errorCode", errorCode);
		m.put("errorType", errorType);
		
		Object obj = baseDao.selectOne(DaoTools.getMapperNamespace(ErrorInfo.class, "selectByCodeOrType"), m);
		return obj == null ? null : (ErrorInfo)obj;
	}

}
