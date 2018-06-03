package com.config.core;

import java.util.ArrayList;
import java.util.List;

public class TableMeta {
	public String name;
	public String remarks;
	public String primaryKey;
	public String primaryKeyJavaType;
	public int len = 1;
	public int big = 0;
	public List<ColumnMeta> columnMetas = new ArrayList<ColumnMeta>();
	public String baseModelName;
	public String baseModelContent;
	public String baseModelKeyCOntent;
	public String modelExampleContent;
	public String baseModelXmlContent;
	public String baseServiceContent;
	public String modelName;
	public String modelContent;
	public int colNameMaxLen = "Field".length();
	public int colTypeMaxLen = "Type".length();
	public int colDefaultValueMaxLen = "Default".length();
}