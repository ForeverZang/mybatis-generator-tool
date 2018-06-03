package com.config.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.config.util.StrKit;

public class ModelExampleGenerator {
	protected String packageTemplate = "package %s;%n%n";
	protected String importTemplate = "import java.util.List;%nimport java.util.ArrayList;%n%n";

	protected String classDefineTemplate = "public class %sExample {%n\tprotected String orderByClause;%n%n\tprotected boolean distinct;%n%n\tprotected List<Criteria> oredCriteria;%n%n\tpublic %sExample(){%n\t\toredCriteria = new ArrayList<Criteria>();%n\t}%n%n\tpublic void setOrderByClause(String orderByClause) {%n\t\tthis.orderByClause = orderByClause;%n\t}%n%n\tpublic String getOrderByClause() {%n\t\treturn orderByClause;%n\t}%n%n\tpublic void setDistinct(boolean distinct) {%n\t\tthis.distinct = distinct;%n\t}%n%n\tpublic boolean isDistinct() {%n\t\treturn distinct;%n\t}%n%n\tpublic List<Criteria> getOredCriteria() {%n\t\treturn oredCriteria;%n\t}%n%n\tpublic void or(Criteria criteria) {%n\t\toredCriteria.add(criteria);%n\t}%n%n\tpublic Criteria or() {%n\t\tCriteria criteria = createCriteriaInternal();%n\t\toredCriteria.add(criteria);%n\t\treturn criteria;%n\t}%n%n\tpublic Criteria createCriteria() {%n\t\tCriteria criteria = createCriteriaInternal();%n\t\tif (oredCriteria.size() == 0) {%n\t\t\toredCriteria.add(criteria);%n\t\t}%n\t\treturn criteria;%n\t}%n%n\tprotected Criteria createCriteriaInternal() {%n\t\tCriteria criteria = new Criteria();%n\t\treturn criteria;%n\t}%n%n\tpublic void clear() {%n\t\toredCriteria.clear();%n\t\torderByClause = null;%n\t\tdistinct = false;%n\t}%n%n\tprotected abstract static class GeneratedCriteria {%n\t\tprotected List<Criterion> criteria;%n%n\t\tprotected GeneratedCriteria() {%n\t\t\tsuper();%n\t\t\tcriteria = new ArrayList<Criterion>();%n\t\t}%n%n\t\tpublic boolean isValid() {%n\t\t\treturn criteria.size() > 0;%n\t\t}%n%n\t\tpublic List<Criterion> getAllCriteria() {%n\t\t\treturn criteria;%n\t\t}%n%n\t\tpublic List<Criterion> getCriteria() {%n\t\t\treturn criteria;%n\t\t}%n%n\t\tprotected void addCriterion(String condition) {%n\t\t\tif (condition == null) {%n\t\t\t\tthrow new RuntimeException(\"Value for condition cannot be null\");%n\t\t\t}%n\t\t\tcriteria.add(new Criterion(condition));%n\t\t}%n%n\t\tprotected void addCriterion(String condition, Object value, String property) {%n\t\t\tif (value == null) {%n\t\t\t\tthrow new RuntimeException(\"Value for \" + property + \" cannot be null\");%n\t\t\t}%n\t\t\tcriteria.add(new Criterion(condition, value));%n\t\t}%n%n\t\tprotected void addCriterion(String condition, Object value1, Object value2, String property) {%n\t\t\tif (value1 == null || value2 == null) {%n\t\t\t\tthrow new RuntimeException(\"Between values for \" + property + \" cannot be null\");%n\t\t\t}%n\t\t\tcriteria.add(new Criterion(condition, value1, value2));%n\t\t}%n%n";

	protected String isNull = "\t\tpublic Criteria and%sIsNull() {%n\t\t\taddCriterion(\"%s is null\");%n\t\t\treturn (Criteria) this;%n\t\t}%n%n";
	protected String isNotNull = "\t\tpublic Criteria and%sIsNotNull() {%n\t\t\taddCriterion(\"%s is not null\");%n\t\t\treturn (Criteria) this;%n\t\t}%n%n";
	protected String equalTo = "\t\tpublic Criteria and%sEqualTo(%s value) {%n\t\t\taddCriterion(\"%s =\", value, \"%s\");%n\t\t\treturn (Criteria) this;%n\t\t}%n%n";
	protected String notEqualTo = "\t\tpublic Criteria and%sNotEqualTo(%s value) {%n\t\t\taddCriterion(\"%s <>\", value, \"%s\");%n\t\t\treturn (Criteria) this;%n\t\t}%n%n";
	protected String greaterThan = "\t\tpublic Criteria and%sGreaterThan(%s value) {%n\t\t\taddCriterion(\"%s >\", value, \"%s\");%n\t\t\treturn (Criteria) this;%n\t\t}%n%n";
	protected String greaterThanOrEqualTo = "\t\tpublic Criteria and%sGreaterThanOrEqualTo(%s value) {%n\t\t\taddCriterion(\"%s >=\", value, \"%s\");%n\t\t\treturn (Criteria) this;%n\t\t}%n%n";
	protected String lessThan = "\t\tpublic Criteria and%sLessThan(%s value) {%n\t\t\taddCriterion(\"%s <\", value, \"%s\");%n\t\t\treturn (Criteria) this;%n\t\t}%n%n";
	protected String lessThanOrEqualTo = "\t\tpublic Criteria and%sLessThanOrEqualTo(%s value) {%n\t\t\taddCriterion(\"%s <=\", value, \"%s\");%n\t\t\treturn (Criteria) this;%n\t\t}%n%n";
	protected String like = "\t\tpublic Criteria and%sLike(%s value) {%n\t\t\taddCriterion(\"%s like\", value, \"%s\");%n\t\t\treturn (Criteria) this;%n\t\t}%n%n";
	protected String notLike = "\t\tpublic Criteria and%sNotLike(%s value) {%n\t\t\taddCriterion(\"%s not like\", value, \"%s\");%n\t\t\treturn (Criteria) this;%n\t\t}%n%n";
	protected String in = "\t\tpublic Criteria and%sIn(List<%s> values) {%n\t\t\taddCriterion(\"%s in\", values, \"%s\");%n\t\t\treturn (Criteria) this;%n\t\t}%n%n";
	protected String notIn = "\t\tpublic Criteria and%sNotIn(List<%s> values) {%n\t\t\taddCriterion(\"%s not in\", values, \"%s\");%n\t\t\treturn (Criteria) this;%n\t\t}%n%n";
	protected String between = "\t\tpublic Criteria and%sBetween(%s value1, %s value2) {%n\t\t\taddCriterion(\"%s between\", value1, value2, \"%s\");%n\t\t\treturn (Criteria) this;%n\t\t}%n%n";
	protected String notBetween = "\t\tpublic Criteria and%sNotBetween(%s value1, %s value2) {%n\t\t\taddCriterion(\"%s not between\", value1, value2, \"%s\");%n\t\t\treturn (Criteria) this;%n\t\t}%n%n";

	protected String classEnd = "\tpublic static class Criteria extends GeneratedCriteria {%n%n\t\tprotected Criteria() {%n\t\t\tsuper();%n\t\t}%n\t}%n%n\tpublic static class Criterion {%n\t\tprivate String condition;%n%n\t\tprivate Object value;%n%n\t\tprivate Object secondValue;%n%n\t\tprivate boolean noValue;%n%n\t\tprivate boolean singleValue;%n%n\t\tprivate boolean betweenValue;%n%n\t\tprivate boolean listValue;%n%n\t\tprivate String typeHandler;%n%n\t\tpublic String getCondition() {%n\t\t\treturn condition;%n\t\t}%n%n\t\tpublic Object getValue() {%n\t\t\treturn value;%n\t\t}%n%n\t\tpublic Object getSecondValue() {%n\t\t\treturn secondValue;%n\t\t}%n%n\t\tpublic boolean isNoValue() {%n\t\treturn noValue;%n\t\t}%n%n\t\tpublic boolean isSingleValue() {%n\t\t\treturn singleValue;%n\t\t}%n%n\t\tpublic boolean isBetweenValue() {%n\t\t\treturn betweenValue;%n\t\t}%n%n\t\tpublic boolean isListValue() {%n\t\t\treturn listValue;%n\t\t}%n%n\t\tpublic String getTypeHandler() {%n\t\t\treturn typeHandler;%n\t\t}%n%n\t\tprotected Criterion(String condition) {%n\t\t\tsuper();%n\t\t\tthis.condition = condition;%n\t\t\tthis.typeHandler = null;%n\t\t\tthis.noValue = true;%n\t\t}%n%n\t\tprotected Criterion(String condition, Object value, String typeHandler) {%n\t\t\tsuper();%n\t\t\tthis.condition = condition;%n\t\t\tthis.value = value;%n\t\t\tthis.typeHandler = typeHandler;%n\t\t\tif (value instanceof List<?>) {%n\t\t\t\tthis.listValue = true;%n\t\t\t} else {%n\t\t\t\tthis.singleValue = true;%n\t\t\t}%n\t\t}%n%n\t\tprotected Criterion(String condition, Object value) {%n\t\t\tthis(condition, value, null);%n\t\t}%n%n\t\tprotected Criterion(String condition, Object value, Object secondValue, String typeHandler) {%n\t\t\tsuper();%n\t\t\tthis.condition = condition;%n\t\t\tthis.value = value;%n\t\t\tthis.secondValue = secondValue;%n\t\t\tthis.typeHandler = typeHandler;%n\t\t\tthis.betweenValue = true;%n\t\t}%n%n\t\tprotected Criterion(String condition, Object value, Object secondValue) {%n\t\t\tthis(condition, value, secondValue, null);%n\t\t}%n\t}%n}";
	protected String baseModelPackageName;
	protected String baseModelOutputDir;
	protected JavaKeyword javaKeyword = new JavaKeyword();

	public ModelExampleGenerator(String baseModelPackageName, String baseModelOutputDir) {
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

	protected void genModelContent(TableMeta tableMeta) {
		StringBuilder ret = new StringBuilder();
		genPackage(ret);
		genImport(ret);
		genClassDefine(tableMeta, ret);
		for (ColumnMeta columnMeta : tableMeta.columnMetas) {
			genCriteria(columnMeta, ret);
		}
		ret.append(String.format("}%n%n", new Object[0]));
		ret.append(String.format(this.classEnd, new Object[0]));
		tableMeta.modelExampleContent = ret.toString();
	}

	protected void genCriteria(ColumnMeta columnMeta, StringBuilder ret) {
		String bigName = StrKit.firstCharToUpperCase(columnMeta.attrName);
		ret.append(String.format(this.isNull, new Object[] { bigName, columnMeta.name }));
		ret.append(String.format(this.isNotNull, new Object[] { bigName, columnMeta.name }));
		ret.append(String.format(this.equalTo, new Object[] { bigName, columnMeta.javaType, columnMeta.name, columnMeta.attrName }));
		ret.append(String.format(this.notEqualTo, new Object[] { bigName, columnMeta.javaType, columnMeta.name, columnMeta.attrName }));
		ret.append(String.format(this.greaterThan, new Object[] { bigName, columnMeta.javaType, columnMeta.name, columnMeta.attrName }));
		ret.append(String.format(this.greaterThanOrEqualTo, new Object[] { bigName, columnMeta.javaType, columnMeta.name, columnMeta.attrName }));
		ret.append(String.format(this.lessThan, new Object[] { bigName, columnMeta.javaType, columnMeta.name, columnMeta.attrName }));
		ret.append(String.format(this.lessThanOrEqualTo, new Object[] { bigName, columnMeta.javaType, columnMeta.name, columnMeta.attrName }));
		if (columnMeta.javaType.equals("String")) {
			ret.append(String.format(this.like, new Object[] { bigName, columnMeta.javaType, columnMeta.name, columnMeta.attrName }));
			ret.append(String.format(this.notLike, new Object[] { bigName, columnMeta.javaType, columnMeta.name, columnMeta.attrName }));
		}
		ret.append(String.format(this.in, new Object[] { bigName, columnMeta.javaType, columnMeta.name, columnMeta.attrName }));
		ret.append(String.format(this.notIn, new Object[] { bigName, columnMeta.javaType, columnMeta.name, columnMeta.attrName }));
		ret.append(String.format(this.between, new Object[] { bigName, columnMeta.javaType, columnMeta.javaType, columnMeta.name, columnMeta.attrName }));
		ret.append(String.format(this.notBetween, new Object[] { bigName, columnMeta.javaType, columnMeta.javaType, columnMeta.name, columnMeta.attrName }));
	}

	protected void genPackage(StringBuilder ret) {
		ret.append(String.format(this.packageTemplate, new Object[] { this.baseModelPackageName }));
	}

	protected void genImport(StringBuilder ret) {
		ret.append(String.format(this.importTemplate, new Object[0]));
	}

	protected void genClassDefine(TableMeta tableMeta, StringBuilder ret) {
		ret.append(String.format(this.classDefineTemplate, new Object[] { tableMeta.baseModelName, tableMeta.baseModelName }));
	}

	protected void generate(List<TableMeta> tableMetas) {
		System.out.println("生成 model example ...");
		for (TableMeta tableMeta : tableMetas)
			genModelContent(tableMeta);
		writeToFile(tableMetas);
	}

	protected void writeToFile(List<TableMeta> tableMetas) {
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
		String target = this.baseModelOutputDir + File.separator + tableMeta.baseModelName + "Example.java";
		FileWriter fw = new FileWriter(target);
		try {
			fw.write(tableMeta.modelExampleContent);
			fw.flush();
		} finally {
			fw.close();
		}
	}
}