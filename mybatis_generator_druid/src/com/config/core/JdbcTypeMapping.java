package com.config.core;

import java.util.HashMap;
import java.util.Map;

public class JdbcTypeMapping {
	protected Map<Integer, String> map = new HashMap<Integer, String>(){
		private static final long serialVersionUID = 1L;
	{
		put(1, "CHAR");
		put(3, "DECIMAL");
		/*put(4, "ID");*/
		put(4, "INTEGER");
		/*put(4, "MEDIUMINT");*/
		put(5, "SMALLINT");
		put(7, "FLOAT");
		put(8, "DOUBLE");
		put(12, "VARCHAR");
		put(-1, "TEXT");
		put(-4, "BLOB");
		put(-5, "BIGINT");
		put(-6, "TINYINT");
		put(-7, "BIT");
		/*put(91, "YEAR");*/
		put(91, "DATE");
		put(92, "TIME");
		put(93, "DATETIME");
		/*put(93, "TIMESTAMP");*/
	}};

	public String getType(Integer typeString) {
		return (String) this.map.get(typeString);
	}
}