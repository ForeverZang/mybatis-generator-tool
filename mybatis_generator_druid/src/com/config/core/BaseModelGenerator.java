package com.config.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.config.util.StrKit;

public class BaseModelGenerator {
	protected String packageTemplate = "package %s;%n%n";

	protected String importTemplate = "%n";

	protected String classDefineTemplate = "%npublic class %s {%n%n";

	protected String propertyTemplate = "\tprivate %s %s;%n%n";

	protected String setterTemplate = "\tpublic void %s(%s %s) {%n\t\tthis.%s=%s;%n\t}%n%n";

	protected String getterTemplate = "\tpublic %s %s() {%n\t\treturn %s;%n\t}%n%n";
	protected String baseModelPackageName;
	protected String baseModelOutputDir;
	protected JavaKeyword javaKeyword = new JavaKeyword();

	public BaseModelGenerator(String baseModelPackageName, String baseModelOutputDir) {
		if (StrKit.isBlank(baseModelPackageName))
			throw new IllegalArgumentException("baseModelPackageName can not be blank.");
		if ((baseModelPackageName.contains("/")) || (baseModelPackageName.contains("\\")))
			throw new IllegalArgumentException("baseModelPackageName error : " + baseModelPackageName);
		if (StrKit.isBlank(baseModelOutputDir)) {
			throw new IllegalArgumentException("baseModelOutputDir can not be blank.");
		}
		this.baseModelPackageName = baseModelPackageName;
		this.baseModelOutputDir = baseModelOutputDir;
	}

	public void generate(List<TableMeta> tableMetas) {
		System.out.println("开始生成模型层dao ...");
		for (TableMeta tableMeta : tableMetas) {
			genBaseModelContent(tableMeta);
		}
		wirtToFile(tableMetas);
	}

	protected void genBaseModelContent(TableMeta tableMeta) {
		StringBuilder ret = new StringBuilder();
		StringBuilder ret2 = new StringBuilder();
		genPackage(ret, ret2);
		genImport(ret, ret2);
		genClassDefine(tableMeta, ret, ret2);
		for (ColumnMeta columnMeta : tableMeta.columnMetas) {
			genProperty(tableMeta, columnMeta, ret, ret2);
		}
		for (ColumnMeta columnMeta : tableMeta.columnMetas) {
			genSetMethodName(tableMeta, columnMeta, ret, ret2);
			genGetMethodName(tableMeta, columnMeta, ret, ret2);
		}
		ret.append(String.format("}%n", new Object[0]));
		ret2.append(String.format("}%n", new Object[0]));
		tableMeta.baseModelContent = ret.toString();
		tableMeta.baseModelKeyCOntent = ret2.toString();
	}

	protected void genPackage(StringBuilder ret, StringBuilder ret2) {
		ret.append(String.format(this.packageTemplate, new Object[] { this.baseModelPackageName }));
		ret2.append(String.format(this.packageTemplate, new Object[] { this.baseModelPackageName }));
	}

	protected void genImport(StringBuilder ret, StringBuilder ret2) {
		ret.append(String.format(this.importTemplate, new Object[0]));
		ret2.append(String.format(this.importTemplate, new Object[0]));
	}

	protected void genClassDefine(TableMeta tableMeta, StringBuilder ret, StringBuilder ret2) {
		if (tableMeta.len <= 1) {
			ret.append(String.format(this.classDefineTemplate, new Object[] { tableMeta.baseModelName }));
		} else {
			ret.append(String.format(this.classDefineTemplate, new Object[] { tableMeta.baseModelName + " extends " + tableMeta.baseModelName + "Key" }));
			ret2.append(String.format(this.classDefineTemplate, new Object[] { tableMeta.baseModelName + "Key" }));
		}
	}

	protected void genSetMethodName(TableMeta tableMeta, ColumnMeta columnMeta, StringBuilder ret, StringBuilder ret2) {
		String setterMethodName = "set" + StrKit.firstCharToUpperCase(columnMeta.attrName);

		String argName = this.javaKeyword.contains(columnMeta.attrName) ? "_" + columnMeta.attrName : columnMeta.attrName;
		String setter = String.format(this.setterTemplate, new Object[] { setterMethodName, columnMeta.javaType, argName, columnMeta.attrName, argName });
		if (isKey(tableMeta, columnMeta)) {
			ret.append("");
			ret2.append(setter);
		} else {
			ret.append(setter);
		}
	}

	protected void genProperty(TableMeta tableMeta, ColumnMeta columnMeta, StringBuilder ret, StringBuilder ret2) {
		String prop = String.format(this.propertyTemplate, new Object[] { columnMeta.javaType, columnMeta.attrName });
		if (isKey(tableMeta, columnMeta)) {
			ret.append("");
			ret2.append(prop);
		} else {
			ret.append(prop);
		}
	}

	private boolean isKey(TableMeta tableMeta, ColumnMeta columnMeta) {
		if (tableMeta.len > 1) {
			String[] keys = tableMeta.primaryKey.split(",");
			for (String key : keys) {
				if (columnMeta.name.equals(key))
					return true;
			}
		}
		return false;
	}

	protected void genGetMethodName(TableMeta tableMeta, ColumnMeta columnMeta, StringBuilder ret, StringBuilder ret2) {
		String getterMethodName = "get" + StrKit.firstCharToUpperCase(columnMeta.attrName);
		String getter = String.format(this.getterTemplate, new Object[] { columnMeta.javaType, getterMethodName, columnMeta.attrName });
		if (isKey(tableMeta, columnMeta)) {
			ret.append("");
			ret2.append(getter);
		} else {
			ret.append(getter);
		}
	}

	protected void wirtToFile(List<TableMeta> tableMetas) {
		try {
			for (TableMeta tableMeta : tableMetas)
				wirtToFile(tableMeta);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected void wirtToFile(TableMeta tableMeta) throws IOException {
		File dir = new File(this.baseModelOutputDir);
		if (!dir.exists())
			dir.mkdirs();
		if (tableMeta.len > 1) {
			String t = this.baseModelOutputDir + File.separator + tableMeta.baseModelName + "Key.java";
			FileWriter f = new FileWriter(t);
			try {
				f.write(tableMeta.baseModelKeyCOntent);
				f.flush();
			} catch (Exception localException) {
			} finally {
				f.close();
			}
		}
		String target = this.baseModelOutputDir + File.separator + tableMeta.baseModelName + ".java";
		FileWriter fw = new FileWriter(target);
		try {
			fw.write(tableMeta.baseModelContent);
			fw.flush();
		} finally {
			fw.close();
		}
	}
}