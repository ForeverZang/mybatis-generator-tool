package com.config.dialect;

public abstract class Dialect {
	public abstract String forTableBuilderDoBuild(String paramString);

	public boolean supportsLimit() {
		return false;
	}

	public boolean supportsLimitOffset() {
		return supportsLimit();
	}

	public abstract String getLimitString(String paramString, int paramInt1, int paramInt2);

	public String getCountString(String sql) {
		return "select count(1) as count from (" + sql + ") temp_table_count";
	}

	public abstract String getOffsetString(String paramString1, String paramString2, String paramString3);
}