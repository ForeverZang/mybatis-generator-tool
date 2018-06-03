package com.config.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.config.util.StrKit;

public class ServiceGenerator {
	protected String packageTemplate = "package %s;%n%n";

	protected String importTemplate = "import java.util.List;%n%nimport org.springframework.beans.factory.annotation.Autowired;%nimport org.springframework.stereotype.Service;%n%nimport %s.%sMapper;%nimport %s.%s;%nimport %s.%sExample;%n";

	protected String keyTemplate = "import %s.%sKey;%n";

	protected String classDefineTemplate = "%n@Service%npublic class %sService {%n";

	protected String mapperTemplate = "\t@Autowired%n\tprivate %sMapper %sMapper;%n%n";

	protected String countByExample = "\tpublic int countByExample(%sExample example) {%n\t\treturn this.%sMapper.countByExample(example);%n\t}%n%n";
	protected String selectByPrimaryKey = "\tpublic %s selectByPrimaryKey(%s %s) {%n\t\treturn this.%sMapper.selectByPrimaryKey(%s);%n\t}%n%n";
	protected String selectByExample = "\tpublic List<%s> selectByExample(%sExample example) {%n\t\treturn this.%sMapper.selectByExample(example);%n\t}%n%n";
	protected String deleteByPrimaryKey = "\tpublic int deleteByPrimaryKey(%s %s) {%n\t\treturn this.%sMapper.deleteByPrimaryKey(%s);%n\t}%n%n";
	protected String updateByPrimaryKeySelective = "\tpublic int updateByPrimaryKeySelective(%s record) {%n\t\treturn this.%sMapper.updateByPrimaryKeySelective(record);%n\t}%n%n";
	protected String updateByPrimaryKey = "\tpublic int updateByPrimaryKey(%s record) {%n\t\treturn this.%sMapper.updateByPrimaryKey(record);%n\t}%n%n";
	protected String deleteByExample = "\tpublic int deleteByExample(%sExample example) {%n\t\treturn this.%sMapper.deleteByExample(example);%n\t}%n%n";
	protected String updateByExampleSelective = "\tpublic int updateByExampleSelective(%s record, %sExample example) {%n\t\treturn this.%sMapper.updateByExampleSelective(record, example);%n\t}%n%n";
	protected String updateByExample = "\tpublic int updateByExample(%s record, %sExample example) {%n\t\treturn this.%sMapper.updateByExample(record, example);%n\t}%n%n";
	protected String insert = "\tpublic int insert(%s record) {%n\t\treturn this.%sMapper.insert(record);%n\t}%n%n";
	protected String insertSelective = "\tpublic int insertSelective(%s record) {%n\t\treturn this.%sMapper.insertSelective(record);%n\t}%n%n";

	protected String selectByExampleWithBLOBs = "\tpublic List<%s> selectByExampleWithBLOBs(%sExample example) {%n\t\treturn this.%sMapper.selectByExampleWithBLOBs(example);%n\t}%n%n";
	protected String updateByExampleWithBLOBs = "\tpublic int updateByExampleWithBLOBs(%s record, %sExample example) {%n\t\treturn this.%sMapper.updateByExampleWithBLOBs(record, example);%n\t}%n%n";
	protected String updateByPrimaryKeyWithBLOBs = "\tpublic int updateByPrimaryKeyWithBLOBs(%s record) {%n\t\treturn this.%sMapper.updateByPrimaryKeyWithBLOBs(record);%n\t}%n%n";
	protected String modelPackageName;
	protected String baseModelPackageName;
	protected String serviceOutputDir;
	protected String baseServicePackageName;

	public ServiceGenerator(String modelPackageName, String baseModelPackageName, String baseServicePackageName, String serviceOutputDir) {
		this.modelPackageName = modelPackageName;
		this.baseModelPackageName = baseModelPackageName;
		this.serviceOutputDir = serviceOutputDir;
		this.baseServicePackageName = baseServicePackageName;
	}

	public void generate(List<TableMeta> tableMetas) {
		System.out.println("生成 service ...");
		for (TableMeta tableMeta : tableMetas)
			genServiceContent(tableMeta);
		wirtToFile(tableMetas);
	}

	public void genServiceContent(TableMeta tableMeta) {
		String small = StrKit.firstCharToLowerCase(tableMeta.modelName);
		String key = tableMeta.primaryKey;
		String keyType = tableMeta.primaryKeyJavaType;
		if (tableMeta.len > 1) {
			key = "key";
			keyType = tableMeta.modelName + "Key";
		}
		StringBuilder ret = new StringBuilder();
		ret.append(String.format(this.packageTemplate, new Object[] { this.baseServicePackageName }));
		ret.append(String.format(this.importTemplate, new Object[] { this.modelPackageName, tableMeta.modelName, this.baseModelPackageName, tableMeta.modelName, this.baseModelPackageName, tableMeta.modelName }));
		if (tableMeta.len > 1) {
			ret.append(String.format(this.keyTemplate, new Object[] { this.baseModelPackageName, tableMeta.modelName }));
		}
		ret.append(String.format(this.classDefineTemplate, new Object[] { tableMeta.modelName }));
		ret.append(String.format(this.mapperTemplate, new Object[] { tableMeta.modelName, small }));
		ret.append(String.format(this.countByExample, new Object[] { tableMeta.modelName, small }));
		ret.append(String.format(this.selectByPrimaryKey, new Object[] { tableMeta.modelName, keyType, key, small, key }));
		ret.append(String.format(this.selectByExample, new Object[] { tableMeta.modelName, tableMeta.modelName, small }));
		if (tableMeta.big > 0) {
			ret.append(String.format(this.selectByExampleWithBLOBs, new Object[] { tableMeta.modelName, tableMeta.modelName, small }));
		}
		ret.append(String.format(this.deleteByPrimaryKey, new Object[] { keyType, key, small, key }));
		ret.append(String.format(this.updateByPrimaryKeySelective, new Object[] { tableMeta.modelName, small }));
		if (tableMeta.big > 0) {
			ret.append(String.format(this.updateByPrimaryKeyWithBLOBs, new Object[] { tableMeta.modelName, small }));
		}
		ret.append(String.format(this.updateByPrimaryKey, new Object[] { tableMeta.modelName, small }));
		ret.append(String.format(this.deleteByExample, new Object[] { tableMeta.modelName, small }));
		ret.append(String.format(this.updateByExampleSelective, new Object[] { tableMeta.modelName, tableMeta.modelName, small }));
		if (tableMeta.big > 0) {
			ret.append(String.format(this.updateByExampleWithBLOBs, new Object[] { tableMeta.modelName, tableMeta.modelName, small }));
		}
		ret.append(String.format(this.updateByExample, new Object[] { tableMeta.modelName, tableMeta.modelName, small }));
		ret.append(String.format(this.insert, new Object[] { tableMeta.modelName, small }));
		ret.append(String.format(this.insertSelective, new Object[] { tableMeta.modelName, small }));
		ret.append(String.format("}%n", new Object[0]));
		tableMeta.baseServiceContent = ret.toString();
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
		File dir = new File(this.serviceOutputDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String target = this.serviceOutputDir + File.separator + tableMeta.modelName + "Service.java";

		File file = new File(target);
		if (file.exists()) {
			return;
		}

		FileWriter fw = new FileWriter(file);
		try {
			fw.write(tableMeta.baseServiceContent);
			fw.flush();
		} finally {
			fw.close();
		}
	}
}