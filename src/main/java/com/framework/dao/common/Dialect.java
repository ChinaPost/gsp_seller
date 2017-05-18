package com.framework.dao.common;

/**
 * @author tang E-mail: killerover84@163.com
 * @version 2016年4月18日 下午1:57:02 类说明:
 * 分页实现本地数据库化，例如mysql，oracle的分页方法
 */
public class Dialect {

	private String dialect = "MYSQL";

	public Dialect(String dialect) {
		// TODO Auto-generated constructor stub
		this.dialect = dialect;
	}

	/*
	 * 生成分页sql
	 */
	public String getLimitString(String sql, int offset, int limit) {
		StringBuffer pageSql = new StringBuffer();

		if (offset < 0 || limit <= 0) {
			return sql;
		} else {
			sql = sql.trim();

			if ("MYSQL".equals(dialect)) {
				sql = sql.trim();
				pageSql.append("SELECT * FROM (").append(sql).append(")  A   limit ")
				.append(offset).append(" , ").append(limit);
			} else {
				sql = sql.trim();
				pageSql.append("SELECT * FROM (SELECT A.*, ROWNUM RN FROM (").append(sql).append(")  A )  WHERE RN<= ")
						.append(offset + limit).append(" AND RN > ").append(offset);
			}

			return pageSql.toString();
		}
	}
}
