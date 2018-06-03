package com.config.dialect;

public class MysqlDialect extends Dialect {
	public boolean supportsLimit() {
		return true;
	}

	public boolean supportsLimitOffset() {
		return true;
	}

	public String getLimitString(String sql, int offset, int limit) {
		sql = sql.trim();
		StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
		pagingSelect.append(sql);
		pagingSelect.append(" limit ").append(offset).append(" , ").append(limit);
		return pagingSelect.toString();
	}

	public String getOffsetString(String sql, String id, String name) {
		sql = sql.trim();
		StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
		pagingSelect.append("select temp.rowno from(SELECT temp_a." + name + ",(@rowno :=@rowno + 1) AS rowno from(");
		pagingSelect.append(sql).append(")temp_a,(SELECT(@rowno := 0)) temp_b)temp ");
		pagingSelect.append("where temp." + name + " = '").append(id).append("'");
		return pagingSelect.toString();
	}

	public String forTableBuilderDoBuild(String tableName) {
		return "select * from `" + tableName + "` where 1 = 2";
	}
}