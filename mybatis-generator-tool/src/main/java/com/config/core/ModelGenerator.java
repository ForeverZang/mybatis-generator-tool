package com.config.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.config.util.StrKit;

public class ModelGenerator {
	protected String packageTemplate = "package %s;%n%n";

	protected String importTemplate = "import java.util.List;%n%nimport org.apache.ibatis.annotations.Param;%n%nimport %s.%s;%nimport %s.%sExample;%n";

	protected String keyTemplate = "import %s.%sKey;%n";

	protected String classDefineTemplate = "%npublic interface %sMapper {%n";

	protected String countByExample = "\tint countByExample(%sExample example);%n%n";
	protected String deleteByExample = "\tint deleteByExample(%sExample example);%n%n";
	protected String deleteByPrimaryKey = "\tint deleteByPrimaryKey(%s %s);%n%n";
	protected String insert = "\tint insert(%s record);%n%n";
	protected String insertSelective = "\tint insertSelective(%s record);%n%n";
	protected String selectByExample = "\tList<%s> selectByExample(%sExample example);%n%n";
	protected String selectByPrimaryKey = "\t%s selectByPrimaryKey(%s %s);%n%n";
	protected String updateByExampleSelective = "\tint updateByExampleSelective(@Param(\"record\") %s record,@Param(\"example\") %sExample example);%n%n";
	protected String updateByExample = "\tint updateByExample(@Param(\"record\") %s record,@Param(\"example\") %sExample example);%n%n";
	protected String updateByPrimaryKeySelective = "\tint updateByPrimaryKeySelective(%s record);%n%n";
	protected String updateByPrimaryKey = "\tint updateByPrimaryKey(%s record);%n%n";

	protected String selectByExampleWithBLOBs = "\tList<%s> selectByExampleWithBLOBs(%sExample example);%n%n";

	protected String updateByExampleWithBLOBs = "\tint updateByExampleWithBLOBs(@Param(\"record\") %s record, @Param(\"example\") %sExample example);%n%n";
	protected String updateByPrimaryKeyWithBLOBs = "\tint updateByPrimaryKeyWithBLOBs(%s record);%n%n";
	protected String modelPackageName;
	protected String baseModelPackageName;
	protected String modelOutputDir;
	protected boolean generateDaoInModel = true;

	public ModelGenerator(String modelPackageName, String baseModelPackageName, String modelOutputDir) {
		if (StrKit.isBlank(modelPackageName))
			throw new IllegalArgumentException("modelPackageName can not be blank.");
		if ((modelPackageName.contains("/")) || (modelPackageName.contains("\\")))
			throw new IllegalArgumentException("modelPackageName error : " + modelPackageName);
		if (StrKit.isBlank(baseModelPackageName))
			throw new IllegalArgumentException("baseModelPackageName can not be blank.");
		if ((baseModelPackageName.contains("/")) || (baseModelPackageName.contains("\\")))
			throw new IllegalArgumentException("baseModelPackageName error : " + baseModelPackageName);
		if (StrKit.isBlank(modelOutputDir)) {
			throw new IllegalArgumentException("modelOutputDir can not be blank.");
		}
		this.modelPackageName = modelPackageName;
		this.baseModelPackageName = baseModelPackageName;
		this.modelOutputDir = modelOutputDir;
	}

	public void setGenerateDaoInModel(boolean generateDaoInModel) {
		this.generateDaoInModel = generateDaoInModel;
	}

	public void generate(List<TableMeta> tableMetas) {
		System.out.println("生成 model ...");
		for (TableMeta tableMeta : tableMetas)
			genModelContent(tableMeta);
		wirtToFile(tableMetas);
	}

	protected void genModelContent(TableMeta tableMeta) {
		StringBuilder ret = new StringBuilder();
		genPackage(ret);
		genImport(tableMeta, ret);
		genClassDefine(tableMeta, ret);
		genDao(tableMeta, ret);
		ret.append(String.format("}%n", new Object[0]));
		tableMeta.modelContent = ret.toString();
	}

	protected void genPackage(StringBuilder ret) {
		ret.append(String.format(this.packageTemplate, new Object[] { this.modelPackageName }));
	}

	protected void genImport(TableMeta tableMeta, StringBuilder ret) {
		ret.append(String.format(this.importTemplate, new Object[] { this.baseModelPackageName, tableMeta.baseModelName, this.baseModelPackageName, tableMeta.baseModelName }));
		if (tableMeta.len > 1)
			ret.append(String.format(this.keyTemplate, new Object[] { this.baseModelPackageName, tableMeta.baseModelName }));
	}

	protected void genClassDefine(TableMeta tableMeta, StringBuilder ret) {
		ret.append(String.format(this.classDefineTemplate, new Object[] { tableMeta.modelName, tableMeta.baseModelName, tableMeta.modelName }));
	}

	protected void genDao(TableMeta tableMeta, StringBuilder ret) {
		if (this.generateDaoInModel) {
			genMapper(tableMeta, ret);
		} else
			ret.append(String.format("\t%n", new Object[0]));
	}

	protected void genMapper(TableMeta tableMeta, StringBuilder ret) {
		String key = tableMeta.primaryKey;
		String keyType = tableMeta.primaryKeyJavaType;
		String bigKey = StrKit.firstCharToUpperCase(tableMeta.modelName);
		ret.append(String.format(this.countByExample, new Object[] { bigKey }));
		ret.append(String.format(this.deleteByExample, new Object[] { bigKey }));
		if (tableMeta.len > 1)
			ret.append(String.format(this.deleteByPrimaryKey, new Object[] { bigKey + "Key", "key" }));
		else {
			ret.append(String.format(this.deleteByPrimaryKey, new Object[] { keyType, key }));
		}
		ret.append(String.format(this.insert, new Object[] { bigKey }));
		ret.append(String.format(this.insertSelective, new Object[] { bigKey }));
		ret.append(String.format(this.selectByExample, new Object[] { bigKey, bigKey }));
		if (tableMeta.big > 0) {
			ret.append(String.format(this.selectByExampleWithBLOBs, new Object[] { bigKey, bigKey }));
		}
		if (tableMeta.len > 1)
			ret.append(String.format(this.selectByPrimaryKey, new Object[] { bigKey, bigKey + "Key", "key" }));
		else {
			ret.append(String.format(this.selectByPrimaryKey, new Object[] { bigKey, keyType, key }));
		}
		ret.append(String.format(this.updateByExampleSelective, new Object[] { bigKey, bigKey }));
		if (tableMeta.big > 0) {
			ret.append(String.format(this.updateByExampleWithBLOBs, new Object[] { bigKey, bigKey }));
		}
		ret.append(String.format(this.updateByExample, new Object[] { bigKey, bigKey }));
		ret.append(String.format(this.updateByPrimaryKeySelective, new Object[] { bigKey }));
		if (tableMeta.big > 0) {
			ret.append(String.format(this.updateByPrimaryKeyWithBLOBs, new Object[] { bigKey }));
		}
		ret.append(String.format(this.updateByPrimaryKey, new Object[] { bigKey }));
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
		File dir = new File(this.modelOutputDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String target = this.modelOutputDir + File.separator + tableMeta.modelName + "Mapper.java";

		FileWriter fw = new FileWriter(target);
		try {
			fw.write(tableMeta.modelContent);
			fw.flush();
		} finally {
			fw.close();
		}
	}
}