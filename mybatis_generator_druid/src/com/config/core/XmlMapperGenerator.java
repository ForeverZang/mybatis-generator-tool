package com.config.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.config.util.StrKit;
import com.config.xml.Attribute;
import com.config.xml.Document;
import com.config.xml.TextElement;
import com.config.xml.XmlElement;

public class XmlMapperGenerator {
	protected String baseModelPackageName;
	protected String baseModelOutputDir;

	public XmlMapperGenerator(String baseModelPackageName, String baseModelOutputDir) {
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

	protected void generate(List<TableMeta> tableMetas) {
		System.out.println("生成 mapper xml ...");
		for (TableMeta tableMeta : tableMetas) {
			genMapperXml(tableMeta);
		}
		writeToFile(tableMetas);
	}

	protected void writeToFile(List<TableMeta> tableMetas) {
		try {
			for (TableMeta tableMeta : tableMetas) {
				wirtToFile(tableMeta);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected void wirtToFile(TableMeta tableMeta) throws IOException {
		File dir = new File(this.baseModelOutputDir);
		if (!dir.exists())
			dir.mkdirs();
		String target = this.baseModelOutputDir + File.separator + tableMeta.baseModelName + "Mapper.xml";
		FileWriter fw = new FileWriter(target);
		try {
			fw.write(tableMeta.baseModelXmlContent);
			fw.flush();
		} finally {
			fw.close();
		}
	}

	protected void genMapperXml(TableMeta tableMeta) {
		XmlElement rootElement = new XmlElement("mapper");
		Document document = new Document("-//mybatis.org//DTD Mapper 3.0//EN", "http://mybatis.org/dtd/mybatis-3-mapper.dtd");

		Attribute attribute = new Attribute("namespace", this.baseModelPackageName + "." + tableMeta.modelName + "Mapper");
		rootElement.addAttribute(attribute);

		rootElement.addElement(genResultMap(tableMeta));
		if (tableMeta.big > 0) {
			rootElement.addElement(genResultMapBlogs(tableMeta));
		}

		rootElement.addElement(sqlElementId("Example_Where_Clause", "oredCriteria"));

		rootElement.addElement(sqlElementId("Update_By_Example_Where_Clause", "example.oredCriteria"));

		rootElement.addElement(baseColumnList(tableMeta));

		if (tableMeta.big > 0) {
			rootElement.addElement(blobColumnList(tableMeta));
			rootElement.addElement(selectByExampleWithBLOBs(tableMeta));
		}

		rootElement.addElement(selctByExample(tableMeta));

		rootElement.addElement(selectByPrimaryKey(tableMeta));

		rootElement.addElement(deleteByPrimaryKey(tableMeta));

		rootElement.addElement(deleteByExample(tableMeta));

		rootElement.addElement(insert(tableMeta));

		rootElement.addElement(insertSelective(tableMeta));

		rootElement.addElement(countByExample(tableMeta));

		rootElement.addElement(updateByExampleSelective(tableMeta));
		if (tableMeta.big > 0) {
			rootElement.addElement(updateByExampleWithBLOBs(tableMeta));
		}

		rootElement.addElement(updateByExample(tableMeta));

		rootElement.addElement(updateByPrimaryKeySelective(tableMeta));
		if (tableMeta.big > 0) {
			rootElement.addElement(updateByPrimaryKeyWithBLOBs(tableMeta));
		}

		rootElement.addElement(updateByPrimaryKey(tableMeta));

		document.setRootElement(rootElement);
		tableMeta.baseModelXmlContent = document.getFormattedContent();
	}

	protected XmlElement updateByPrimaryKey(TableMeta tableMeta) {
		XmlElement update = updateElement("updateByPrimaryKey");
		update.addElement(textElement("update " + tableMeta.name));
		List<ColumnMeta> list = tableMeta.columnMetas;
		int j = 0;
		int len = list.size() - tableMeta.big - tableMeta.len;
		for (int i = 0; i < list.size(); i++)
			if (!((ColumnMeta) list.get(i)).isPrimaryKey) {
				if (((ColumnMeta) list.get(i)).jdbcType != -1) {
					String str = ((ColumnMeta) list.get(i)).name + " = #{" + ((ColumnMeta) list.get(i)).attrName + "}";
					if (j == 0)
						str = "set " + str;
					if (j < len - 1)
						str = str + ",";
					j++;
					update.addElement(textElement(str));
				}
			}
		addPrimaryKeyElement(update, tableMeta);
		return update;
	}

	protected XmlElement updateByPrimaryKeyWithBLOBs(TableMeta tableMeta) {
		XmlElement update = updateElement("updateByPrimaryKeyWithBLOBs");
		update.addElement(textElement("update " + tableMeta.name));
		List<ColumnMeta> list = tableMeta.columnMetas;
		int j = 0;
		int len = list.size() - tableMeta.len;
		for (int i = 0; i < list.size(); i++)
			if (!((ColumnMeta) list.get(i)).isPrimaryKey) {
				String str = ((ColumnMeta) list.get(i)).name + " = #{" + ((ColumnMeta) list.get(i)).attrName + "}";
				if (j == 0)
					str = "set " + str;
				if (j < len - 1)
					str = str + ",";
				j++;
				update.addElement(textElement(str));
			}
		addPrimaryKeyElement(update, tableMeta);
		return update;
	}

	protected XmlElement updateByPrimaryKeySelective(TableMeta tableMeta) {
		XmlElement update = updateElement("updateByPrimaryKeySelective");
		update.addElement(textElement("update " + tableMeta.name));
		XmlElement set = new XmlElement("set");
		List<ColumnMeta> list = tableMeta.columnMetas;
		for (int i = 0; i < list.size(); i++)
			if (!((ColumnMeta) list.get(i)).isPrimaryKey) {
				String str = ((ColumnMeta) list.get(i)).name + " = #{" + ((ColumnMeta) list.get(i)).attrName + "},";
				set.addElement(ifElement(((ColumnMeta) list.get(i)).attrName + " != null", str));
			}
		update.addElement(set);
		addPrimaryKeyElement(update, tableMeta);
		return update;
	}

	protected XmlElement updateByExample(TableMeta tableMeta) {
		XmlElement updateByExample = updateElement("updateByExample");
		updateByExample.addElement(textElement("update " + tableMeta.name));
		List<ColumnMeta> list = tableMeta.columnMetas;
		int j = list.size() - tableMeta.big;
		int k = 0;
		for (int i = 0; i < list.size(); i++)
			if (((ColumnMeta) list.get(i)).jdbcType != -1) {
				String str = ((ColumnMeta) list.get(i)).name + " = #{" + ((ColumnMeta) list.get(i)).attrName + "}";
				if (k == 0) {
					str = "set " + str;
				}
				if (k < j - 1) {
					str = str + ",";
				}
				k++;
				updateByExample.addElement(textElement(str));
			}
		XmlElement iElement = ifElement("_parameter != null", null);
		iElement.addElement(include("Update_By_Example_Where_Clause"));
		updateByExample.addElement(iElement);
		return updateByExample;
	}

	protected XmlElement updateByExampleWithBLOBs(TableMeta tableMeta) {
		XmlElement updateByExample = updateElement("updateByExampleWithBLOBs");
		updateByExample.addElement(textElement("update " + tableMeta.name));
		List<ColumnMeta> list = tableMeta.columnMetas;
		for (int i = 0; i < list.size(); i++) {
			String str = ((ColumnMeta) list.get(i)).name + " = #{" + ((ColumnMeta) list.get(i)).attrName + "}";
			if (i == 0) {
				str = "set " + str;
			}
			if (i < list.size() - 1) {
				str = str + ",";
			}
			updateByExample.addElement(textElement(str));
		}
		XmlElement iElement = ifElement("_parameter != null", null);
		iElement.addElement(include("Update_By_Example_Where_Clause"));
		updateByExample.addElement(iElement);
		return updateByExample;
	}

	protected XmlElement updateByExampleSelective(TableMeta tableMeta) {
		XmlElement updateByExampleSelective = updateElement("updateByExampleSelective");
		updateByExampleSelective.addElement(textElement("update " + tableMeta.name));
		List<ColumnMeta> list = tableMeta.columnMetas;
		XmlElement set = new XmlElement("set");
		for (int i = 0; i < list.size(); i++) {
			set.addElement(ifElement("record." + ((ColumnMeta) list.get(i)).attrName + " != null", ((ColumnMeta) list.get(i)).name + " = " + "#{record." + ((ColumnMeta) list.get(i)).attrName + "},"));
		}
		updateByExampleSelective.addElement(set);

		XmlElement iElement = ifElement("_parameter != null", null);
		iElement.addElement(include("Update_By_Example_Where_Clause"));
		updateByExampleSelective.addElement(iElement);
		return updateByExampleSelective;
	}

	protected XmlElement updateElement(String id) {
		XmlElement xmlElement = new XmlElement("update");
		xmlElement.addAttribute(addAttribute("id", id));
		return xmlElement;
	}

	protected XmlElement countByExample(TableMeta tableMeta) {
		XmlElement count = selectElement("countByExample", null);
		count.addAttribute(addAttribute("resultType", "java.lang.Integer"));
		count.addElement(textElement("select count(*) from " + tableMeta.name));
		XmlElement ifElement = ifElement("_parameter != null", null);
		ifElement.addElement(include("Example_Where_Clause"));
		count.addElement(ifElement);
		return count;
	}

	protected XmlElement insertSelective(TableMeta tableMeta) {
		XmlElement insertSelective = insertElement("insertSelective");
		insertSelective.addElement(textElement("insert into " + tableMeta.name));

		List<ColumnMeta> list = tableMeta.columnMetas;

		XmlElement trim = new XmlElement("trim");
		trim.addAttribute(addAttribute("prefix", "("));
		trim.addAttribute(addAttribute("suffix", ")"));
		trim.addAttribute(addAttribute("suffixOverrides", ","));
		for (int i = 0; i < list.size(); i++) {
			trim.addElement(ifElement(((ColumnMeta) list.get(i)).attrName + " != null", ((ColumnMeta) list.get(i)).name + ","));
		}

		XmlElement t = new XmlElement("trim");
		t.addAttribute(addAttribute("prefix", "values ("));
		t.addAttribute(addAttribute("suffix", ")"));
		t.addAttribute(addAttribute("suffixOverrides", ","));
		for (int i = 0; i < list.size(); i++) {
			t.addElement(ifElement(((ColumnMeta) list.get(i)).attrName + " != null", "#{" + ((ColumnMeta) list.get(i)).attrName + "},"));
		}

		insertSelective.addElement(trim);
		insertSelective.addElement(t);
		return insertSelective;
	}

	protected XmlElement insert(TableMeta tableMeta) {
		XmlElement insert = insertElement("insert");
		List<ColumnMeta> list = tableMeta.columnMetas;
		int line = list.size() / 3 + (list.size() % 3 == 0 ? 0 : 1);
		String[] p = new String[line];
		String[] q = new String[line];
		for (int i = 1; i <= line; i++) {
			StringBuffer sb = new StringBuffer();
			StringBuffer sa = new StringBuffer();
			for (int j = 2 * i - 2; j <= 2 * i; j++) {
				if (i + j > list.size())
					break;
				sb.append(((ColumnMeta) list.get(i + j - 1)).name);
				sa.append("#{" + ((ColumnMeta) list.get(i + j - 1)).attrName + "}");

				if (i + j < list.size()) {
					sb.append(",");
					sa.append(",");
				}
			}
			p[(i - 1)] = sb.toString();
			q[(i - 1)] = sa.toString();
		}
		for (int i = 1; i <= line; i++) {
			if (i == 1)
				insert.addElement(textElement("insert into " + tableMeta.name + " (" + p[0]));
			else {
				insert.addElement(textElement(p[(i - 1)]));
			}
		}
		insert.addElement(textElement(")"));
		for (int i = 1; i <= line; i++) {
			if (i == 1)
				insert.addElement(textElement("values (" + q[0]));
			else {
				insert.addElement(textElement(q[(i - 1)]));
			}
		}
		insert.addElement(textElement(")"));
		return insert;
	}

	protected void addParam(XmlElement element, String params) {
	}

	protected XmlElement insertElement(String id) {
		XmlElement xmlElement = new XmlElement("insert");
		xmlElement.addAttribute(addAttribute("id", id));
		return xmlElement;
	}

	protected XmlElement deleteByExample(TableMeta tableMeta) {
		XmlElement deleteByExample = deleteElement("deleteByExample");
		deleteByExample.addElement(textElement("delete from " + tableMeta.name));
		XmlElement ifeElement = ifElement("_parameter != null", null);
		ifeElement.addElement(include("Example_Where_Clause"));
		deleteByExample.addElement(ifeElement);
		return deleteByExample;
	}

	protected XmlElement deleteByPrimaryKey(TableMeta tableMeta) {
		XmlElement deleteByPrimaryKey = deleteElement("deleteByPrimaryKey");
		deleteByPrimaryKey.addElement(textElement("delete from " + tableMeta.name));
		addPrimaryKeyElement(deleteByPrimaryKey, tableMeta);
		return deleteByPrimaryKey;
	}

	protected XmlElement deleteElement(String id) {
		XmlElement xmlElement = new XmlElement("delete");
		xmlElement.addAttribute(addAttribute("id", id));
		return xmlElement;
	}

	protected void addPrimaryKeyElement(XmlElement element, TableMeta tableMeta) {
		String[] keys = tableMeta.primaryKey.split(",");
		for (int i = 0; i < keys.length; i++) {
			if (i == 0) {
				element.addElement(textElement(paramWhere(keys[i])));
			} else {
				element.addElement(textElement(paramAnd(keys[i])));
			}
		}
	}

	protected XmlElement selectByPrimaryKey(TableMeta tableMeta) {

		XmlElement selectByPrimaryKey = null;
		if (tableMeta.big > 0) {
			selectByPrimaryKey = selectElement("selectByPrimaryKey", "ResultMapWithBLOBs");
		} else {
			selectByPrimaryKey = selectElement("selectByPrimaryKey", "BaseResultMap");
		}
		selectByPrimaryKey.addElement(include("Base_Column_List"));
		if (tableMeta.big > 0) {
			selectByPrimaryKey.addElement(textElement(","));
			selectByPrimaryKey.addElement(include("Blob_Column_List"));
		}
		selectByPrimaryKey.addElement(textElement("from " + tableMeta.name));
		addPrimaryKeyElement(selectByPrimaryKey, tableMeta);
		return selectByPrimaryKey;
		
	}

	protected String paramWhere(String key) {
		return "where " + key + " = #{" + StrKit.toCamelCase(key) + "}";
	}

	protected String paramAnd(String key) {
		return "and " + key + " = #{" + StrKit.toCamelCase(key) + "}";
	}

	protected XmlElement selctByExample(TableMeta tableMeta) {
		XmlElement selectbyExample = selectElement("selectByExample", "BaseResultMap");
		XmlElement distinct = ifElement("distinct", "distinct");
		selectbyExample.addElement(distinct);
		selectbyExample.addElement(include("Base_Column_List"));
		selectbyExample.addElement(textElement("from " + tableMeta.name));

		XmlElement _parameter = ifElement("_parameter != null", null);
		XmlElement _parameterInclue = include("Example_Where_Clause");
		_parameter.addElement(_parameterInclue);
		selectbyExample.addElement(_parameter);

		XmlElement orderByClause = ifElement("orderByClause != null", "order by ${orderByClause}");
		selectbyExample.addElement(orderByClause);
		return selectbyExample;
	}

	protected XmlElement selectByExampleWithBLOBs(TableMeta tableMeta) {
		XmlElement selectbyExample = selectElement("selectByExampleWithBLOBs", "ResultMapWithBLOBs");
		XmlElement distinct = ifElement("distinct", "distinct");
		selectbyExample.addElement(distinct);
		selectbyExample.addElement(include("Base_Column_List"));
		selectbyExample.addElement(textElement(","));
		selectbyExample.addElement(include("Blob_Column_List"));
		selectbyExample.addElement(textElement("from " + tableMeta.name));

		XmlElement _parameter = ifElement("_parameter != null", null);
		XmlElement _parameterInclue = include("Example_Where_Clause");
		_parameter.addElement(_parameterInclue);
		selectbyExample.addElement(_parameter);

		XmlElement orderByClause = ifElement("orderByClause != null", "order by ${orderByClause}");
		selectbyExample.addElement(orderByClause);
		return selectbyExample;
	}

	protected XmlElement include(String refid) {
		XmlElement xmlElement = new XmlElement("include");
		xmlElement.addAttribute(addAttribute("refid", refid));
		return xmlElement;
	}

	protected TextElement textElement(String content) {
		return new TextElement(content);
	}

	protected XmlElement selectElement(String id, String resultMap) {
		XmlElement select = new XmlElement("select");
		select.addAttribute(addAttribute("id", id));
		if (resultMap != null) {
			select.addAttribute(addAttribute("resultMap", resultMap));
			select.addElement(textElement("select"));
		}
		return select;
	}

	protected XmlElement baseColumnList(TableMeta tableMeta) {
		XmlElement base = new XmlElement("sql");
		base.addAttribute(addAttribute("id", "Base_Column_List"));
		StringBuffer sb = new StringBuffer();
		int z = 0;
		int j = tableMeta.columnMetas.size() - tableMeta.big;
		for (int i = 0; i < tableMeta.columnMetas.size(); i++)
			if (((ColumnMeta) tableMeta.columnMetas.get(i)).jdbcType != -1) {
				sb.append(((ColumnMeta) tableMeta.columnMetas.get(i)).name);
				if (z < j - 1) {
					sb.append(",");
				}
				z++;
			}
		base.setContent(sb.toString());
		return base;
	}

	protected XmlElement blobColumnList(TableMeta tableMeta) {
		XmlElement base = new XmlElement("sql");
		base.addAttribute(addAttribute("id", "Blob_Column_List"));
		StringBuffer sb = new StringBuffer();
		int j = tableMeta.big;
		for (int i = 0; i < tableMeta.columnMetas.size(); i++) {
			if (((ColumnMeta) tableMeta.columnMetas.get(i)).jdbcType == -1) {
				sb.append(((ColumnMeta) tableMeta.columnMetas.get(i)).name);
				if (i < j - 1) {
					sb.append(",");
				}
			}
		}
		base.setContent(sb.toString());
		return base;
	}

	protected XmlElement genResultMap(TableMeta tableMeta) {
		XmlElement resultMap = new XmlElement("resultMap");
		Attribute resultMapA1 = new Attribute("id", "BaseResultMap");
		Attribute resultMapA2 = new Attribute("type", this.baseModelPackageName + ".model." + tableMeta.modelName);
		resultMap.addAttribute(resultMapA1);
		resultMap.addAttribute(resultMapA2);
		List<ColumnMeta> list = tableMeta.columnMetas;
		for (ColumnMeta columnMeta : list)
			if (columnMeta.jdbcType != -1) {
				resultMap.addElement(genResultMapSon(columnMeta));
			}
		return resultMap;
	}

	protected XmlElement genResultMapBlogs(TableMeta tableMeta) {
		XmlElement resultMap = new XmlElement("resultMap");
		resultMap.addAttribute(addAttribute("id", "ResultMapWithBLOBs"));
		resultMap.addAttribute(addAttribute("type", this.baseModelPackageName + ".model." + tableMeta.modelName));
		resultMap.addAttribute(addAttribute("extends", "BaseResultMap"));
		List<ColumnMeta> list = tableMeta.columnMetas;
		for (ColumnMeta columnMeta : list) {
			if (columnMeta.jdbcType == -1) {
				resultMap.addElement(genResultMapSon(columnMeta));
			}
		}
		return resultMap;
	}

	protected XmlElement genResultMapSon(ColumnMeta columnMeta) {
		XmlElement resultMapSon = null;
		if (columnMeta.isPrimaryKey)
			resultMapSon = new XmlElement("id");
		else {
			resultMapSon = new XmlElement("result");
		}
		Attribute column = new Attribute("column", columnMeta.name);
		Attribute property = new Attribute("property", columnMeta.attrName);
		resultMapSon.addAttribute(column);
		resultMapSon.addAttribute(property);
		return resultMapSon;
	}

	protected XmlElement sqlElementId(String id, String type) {
		XmlElement sqlId = new XmlElement("sql");
		sqlId.addAttribute(new Attribute("id", id));
		XmlElement where = new XmlElement("where");
		XmlElement foreach = addForeach(type, "criteria", "or", null, null);
		XmlElement ifElement = ifElement("criteria.valid", null);
		XmlElement trim = new XmlElement("trim");
		trim.addAttribute(new Attribute("prefix", "("));
		trim.addAttribute(new Attribute("suffix", ")"));
		trim.addAttribute(new Attribute("prefixOverrides", "and"));

		XmlElement trimEach = addForeach("criteria.criteria", "criterion", null, null, null);

		XmlElement choose = addChoose();
		choose.addElement(addWhen("criterion.noValue", "and ${criterion.condition}"));
		choose.addElement(addWhen("criterion.singleValue", "and ${criterion.condition} #{criterion.value}"));
		choose.addElement(addWhen("criterion.betweenValue", "and ${criterion.condition} #{criterion.value} and #{criterion.secondValue}"));
		XmlElement wElement = addWhen("criterion.listValue", "and ${criterion.condition}");
		XmlElement wElement2 = addForeach("criterion.value", "listItem", ",", "(", ")");
		wElement2.setContent("#{listItem}");
		wElement.addElement(wElement2);
		choose.addElement(wElement);

		trimEach.addElement(choose);
		trim.addElement(trimEach);
		ifElement.addElement(trim);
		foreach.addElement(ifElement);
		where.addElement(foreach);

		sqlId.addElement(where);
		return sqlId;
	}

	protected XmlElement ifElement(String test, String content) {
		XmlElement ifElement = new XmlElement("if");
		ifElement.addAttribute(addAttribute("test", test));
		if (content != null) {
			ifElement.addElement(textElement(content));
		}
		return ifElement;
	}

	protected XmlElement addWhen(String value, String content) {
		XmlElement when = new XmlElement("when");
		when.addAttribute(new Attribute("test", value));
		if (content != null) {
			when.setContent(content);
		}
		return when;
	}

	protected XmlElement addChoose() {
		return new XmlElement("choose");
	}

	protected XmlElement addForeach(String collection, String item, String separator, String open, String close) {
		XmlElement foreach = new XmlElement("foreach");
		if (collection != null)
			foreach.addAttribute(addAttribute("collection", collection));
		if (item != null) {
			foreach.addAttribute(addAttribute("item", item));
		}
		if (open != null) {
			foreach.addAttribute(addAttribute("open", open));
		}
		if (close != null) {
			foreach.addAttribute(addAttribute("close", close));
		}
		if (separator != null) {
			foreach.addAttribute(addAttribute("separator", separator));
		}
		return foreach;
	}

	protected Attribute addAttribute(String key, String value) {
		return new Attribute(key, value);
	}
}