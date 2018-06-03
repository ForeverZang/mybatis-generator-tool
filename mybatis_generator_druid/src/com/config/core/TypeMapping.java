package com.config.core;

import java.util.HashMap;
import java.util.Map;

public class TypeMapping {
	protected Map<String, String> map = new HashMap<String, String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	{
		put("java.lang.Byte", "Byte");
		put("java.lang.Short", "Short");
		put("java.lang.Integer", "Integer");
		put("java.lang.Long", "Long");
		put("java.lang.Float", "Float");
		put("java.lang.Double", "Double");
		put("java.lang.Float", "Float");
		put("java.math.BigInteger", "java.math.BigInteger");
		put("java.math.BigDecimal", "java.math.BigDecimal");
		put("java.lang.Character", "Character");
		put("java.lang.Boolean", "Boolean");
		put("java.lang.String", "String");
		put("java.sql.Date", "java.util.Date");
		put("java.sql.Time", "java.util.Date");
		put("java.sql.Timestamp", "java.util.Date");
	}};

	public String getType(String typeString) {
		return (String) this.map.get(typeString);
	}
}