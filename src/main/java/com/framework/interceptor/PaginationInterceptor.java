package com.framework.interceptor;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.logging.Log;  
import org.apache.commons.logging.LogFactory;  
import org.apache.ibatis.executor.statement.PreparedStatementHandler;  
import org.apache.ibatis.executor.statement.RoutingStatementHandler;  
import org.apache.ibatis.executor.statement.StatementHandler;  
import org.apache.ibatis.mapping.BoundSql;  
import org.apache.ibatis.mapping.ParameterMapping;  
import org.apache.ibatis.plugin.*;  
import org.apache.ibatis.session.Configuration;  
import org.apache.ibatis.session.RowBounds;  

import com.framework.dao.common.CountHelper;
import com.framework.dao.common.Dialect;
import com.framework.dao.common.MybatisHepler;

import java.sql.Connection;  
import java.util.Properties;  
import java.util.regex.Matcher;  
import java.util.regex.Pattern;  
  
/** 
 * 默认情况下 
 * 数据库的类型 
 * 用于分页的拦截器
 * @author : tang 
 * @date: 2016-04-18 
 */  
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})  
public class PaginationInterceptor implements Interceptor {  
  
    private final static Log logger = LogFactory.getLog(PaginationInterceptor.class);  
    public static final String CONFIGURATION = "configuration";  
    private static Dialect dialect = null;  
    private static final String ROW_BOUNDS = "rowBounds";  
    private static final String BOUND_SQL = "boundSql";  
    private static final String DIALECT = "dialect";  
    private static final String SQL = "sql";  
    private static final String OFFSET = "offset";  
    private static final String LIMIT = "limit";  
    public static final String DELEGATE = "delegate";  
    private static final int CONNECTION_INDEX = 0;  
  
    private static final String INTERCEPTOR_CONF = "<plugins>\n" +  
            "<plugin interceptor=\"" + PaginationInterceptor.class.getCanonicalName() + "\">\n" +  
            "<property name=\"dialect\" value=\"MYSQL|ORACLE|...\"/>\n" +  
            "</plugin>\n" +  
            "</plugins>";  
  
    @Override  
    public Object intercept(Invocation invocation) throws Throwable {  
        RoutingStatementHandler statementHandler = (RoutingStatementHandler) invocation.getTarget();  
        PreparedStatementHandler preparedStatHandler =  
                (PreparedStatementHandler) FieldUtils.readField(statementHandler, DELEGATE, true);  
        final Object[] queryArgs = invocation.getArgs();  
        Connection connection = (Connection) queryArgs[CONNECTION_INDEX];  
  
        RowBounds rowBounds = (RowBounds) FieldUtils.readField(preparedStatHandler, ROW_BOUNDS, true);  
        BoundSql boundSql = (BoundSql) FieldUtils.readField(preparedStatHandler, BOUND_SQL, true);  
  
        Configuration configuration = (Configuration) FieldUtils.readField(preparedStatHandler, CONFIGURATION, true);  
  
        //没有分页，直接返回原调用  
        if (rowBounds == null || rowBounds == RowBounds.DEFAULT) {  
            return invocation.proceed();  
        }  
  
        //有分页  
        String originalSql = boundSql.getSql();  
  
        //1.获取总行数，将行数绑定到当前线程中  
        String countSql = MybatisHepler.getCountSql(originalSql);  
        CountHelper.getCount(countSql, preparedStatHandler, configuration, boundSql, connection);  
  
  
        //2.获取分页的结果集  
        //////////////////////////////////////////////////////////////////////////////////////////////  
        String pagingSql = dialect.getLimitString(originalSql, rowBounds.getOffset(), rowBounds.getLimit());  
        FieldUtils.writeField(boundSql, SQL, pagingSql, true);  
  
  
        int size = 0;  
        size = getPageParamNum(originalSql, pagingSql);  
  
        if (size == 1) {  
            ParameterMapping.Builder builder = new ParameterMapping.Builder(configuration, LIMIT, Integer.class);  
            boundSql.getParameterMappings().add(builder.build());  
            boundSql.setAdditionalParameter(LIMIT, rowBounds.getLimit());  
        }  
        if (size == 2) {  
  
            ParameterMapping.Builder builder = new ParameterMapping.Builder(  
                    configuration, OFFSET, Integer.class);  
            boundSql.getParameterMappings().add(builder.build());  
            boundSql.setAdditionalParameter(OFFSET, rowBounds.getOffset());  
  
            builder = new ParameterMapping.Builder(configuration, LIMIT,  
                    Integer.class);  
            boundSql.getParameterMappings().add(builder.build());  
            boundSql.setAdditionalParameter(LIMIT, rowBounds.getLimit());  
        }  
        FieldUtils.writeField(rowBounds, OFFSET, RowBounds.NO_ROW_OFFSET, true);  
        FieldUtils.writeField(rowBounds, LIMIT, RowBounds.NO_ROW_LIMIT, true);  
  
        if (logger.isDebugEnabled()) {  
            logger.debug("\n" + originalSql +  
                    "\n对应的分页SQL:\n" +  
                    boundSql.getSql() +  
                    "\n对应的count SQL:\n" +  
                    countSql);  
        }  
        return invocation.proceed();  
    }  
  
    /** 
     * 获取用于控制分页的问号的个数 
     * 
     * @param originalSql 
     * @param pagingSql 
     * @return 
     */  
    private int getPageParamNum(String originalSql, String pagingSql) {  
        int size = 0;  
        String addSql = pagingSql.replace(originalSql, "");  
  
        Pattern pattern = Pattern.compile("[?]");  
        Matcher matcher = pattern.matcher(addSql);  
        while (matcher.find()) {  
            size++;  
        }  
        return size;  
    }  
  
    @Override  
    public Object plugin(Object target) {  
        return Plugin.wrap(target, this);  
    }  
  
    @Override  
    public void setProperties(Properties properties) {  
        if (PaginationInterceptor.dialect == null) {  
            String dialect = properties.getProperty(DIALECT);  
            if (dialect == null) {  
                throw new RuntimeException("拦截器未提供dialect的配置，正确配置参见：\n" + INTERCEPTOR_CONF);  
            }  
            PaginationInterceptor.dialect = new Dialect(dialect);  
        }  
    }  
}