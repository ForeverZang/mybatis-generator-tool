package com.config.dialect;

public class OracleDialect extends Dialect {
	public boolean supportsLimit() {
		return true;
	}

	public boolean supportsLimitOffset() {
		return true;
	}

	public String getLimitString(String sql, int offset, int limit) {
		sql = sql.trim();
		StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
		if (offset > 0)
			pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
		else {
			pagingSelect.append("select * from ( ");
		}
		pagingSelect.append(sql);
		if (offset > 0) {
			String endString = String.valueOf(offset) + "+" + String.valueOf(limit);
			pagingSelect.append(" ) row_ ) where rownum_ <= " + endString + " and rownum_ > " + String.valueOf(offset));
		} else {
			pagingSelect.append(" ) where rownum <= " + String.valueOf(limit));
		}
		return pagingSelect.toString();
	}

	public String getOffsetString(String sql, String id, String name) {
		return null;
	}

	public String forTableBuilderDoBuild(String tableName) {
		return "select * from " + tableName + " where rownum < 1";
	}
}