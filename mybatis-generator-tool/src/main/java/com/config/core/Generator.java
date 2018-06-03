package com.config.core;

import java.io.File;
import java.util.List;

import javax.sql.DataSource;

import com.config.data.DataAccount;
import com.config.data.DruidPlugin;
import com.config.dialect.Dialect;
import com.config.util.Prop;
import com.config.util.PropUtil;
import com.config.util.StrKit;

public class Generator {
	protected MetaBuilder metaBuilder;
	protected BaseModelGenerator baseModelGenerator;
	protected ModelGenerator modelGenerator;
	protected ModelExampleGenerator modelExampleGenerator;
	protected XmlMapperGenerator xmlMapperGenerator;
	protected ServiceGenerator serviceGenerator;
	protected boolean generateService = true;
	private String url;
	private String account;
	private String password;
	private String projectPath = null;

	public Generator(DataSource dataSource, String baseModelPackageName, String baseModelOutputDir, String modelPackageName, String modelOutputDir) {
		this(dataSource, new BaseModelGenerator(baseModelPackageName, baseModelOutputDir), new ModelGenerator(modelPackageName, baseModelPackageName, modelOutputDir), new ModelExampleGenerator(baseModelPackageName, baseModelOutputDir), new XmlMapperGenerator(modelPackageName, modelOutputDir + "/mapper/"));
	}

	public Generator(DataAccount dataAccount, String sourceRoot, String packageName, String servicePackageName) {
		this(dataAccount.getJdbcUrl(), dataAccount.getJdbcName(), dataAccount.getJdbcPassword(), sourceRoot, packageName, servicePackageName);
	}

	public Generator(String jdbcUrl, String username, String password, String sourceRoot, String modelPackageName, String servicePackageName, String projectPath) {
		this.url = jdbcUrl;
		this.account = username;
		this.password = password;
		this.projectPath = projectPath;
		String baseModelPackageName = modelPackageName + ".model";

		String baseModelOutputDir = getProjectPath() + "/" + sourceRoot + "/" + baseModelPackageName.replace(".", "/");
		String modelOutputDir = baseModelOutputDir + "/..";
		String serviceOutputDir = getProjectPath() + "/" + sourceRoot + "/" + servicePackageName.replace(".", "/");
		DataSource dataSource = getDataSource();
		if (dataSource == null) {
			throw new IllegalArgumentException("dataSource can not be null.");
		}
		this.metaBuilder = new MetaBuilder(dataSource);
		this.baseModelGenerator = new BaseModelGenerator(baseModelPackageName, baseModelOutputDir);
		if (this.baseModelGenerator == null) {
			throw new IllegalArgumentException("baseModelGenerator can not be null.");
		}
		this.modelGenerator = new ModelGenerator(modelPackageName, baseModelPackageName, modelOutputDir);
		if (this.modelGenerator == null) {
			throw new IllegalArgumentException("modelGenerator can not be null.");
		}
		this.modelExampleGenerator = new ModelExampleGenerator(baseModelPackageName, baseModelOutputDir);
		if (this.modelExampleGenerator == null) {
			throw new IllegalArgumentException("modelExampleGenerator can not be null.");
		}
		this.xmlMapperGenerator = new XmlMapperGenerator(modelPackageName, modelOutputDir + "/mapper/");
		if (this.xmlMapperGenerator == null) {
			throw new IllegalArgumentException("xmlMapperGenerator can not be null.");
		}

		this.serviceGenerator = new ServiceGenerator(modelPackageName, baseModelPackageName, servicePackageName, serviceOutputDir);
		if (this.serviceGenerator == null)
			throw new IllegalArgumentException("serviceGenerator can not be null.");
	}

	public Generator(String jdbcUrl, String accout, String password, String sourceRoot, String modelPackageName, String servicePackageName) {
		this(jdbcUrl, accout, password, sourceRoot, modelPackageName, servicePackageName, null);
	}

	private DataSource getDataSource() {
		DruidPlugin druidPlugin = createDruidPlugin();
		druidPlugin.start();
		return druidPlugin.getDataSource();
	}

	public DruidPlugin createDruidPlugin() {
		return new DruidPlugin(this.url, this.account, this.password);
	}

	private String getProjectPath() {
		return this.projectPath == null ? System.getProperty("user.dir") : this.projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public Generator(DataSource dataSource, String baseModelPackageName, String baseModelOutputDir) {
		this(dataSource, new BaseModelGenerator(baseModelPackageName, baseModelOutputDir));
	}

	public Generator(DataSource dataSource, BaseModelGenerator baseModelGenerator) {
		if (dataSource == null) {
			throw new IllegalArgumentException("dataSource can not be null.");
		}
		if (baseModelGenerator == null) {
			throw new IllegalArgumentException("baseModelGenerator can not be null.");
		}

		this.metaBuilder = new MetaBuilder(dataSource);
		this.baseModelGenerator = baseModelGenerator;
		this.modelGenerator = null;
	}

	public Generator(DataSource dataSource, BaseModelGenerator baseModelGenerator, ModelGenerator modelGenerator) {
		if (dataSource == null) {
			throw new IllegalArgumentException("dataSource can not be null.");
		}
		if (baseModelGenerator == null) {
			throw new IllegalArgumentException("baseModelGenerator can not be null.");
		}
		if (modelGenerator == null) {
			throw new IllegalArgumentException("modelGenerator can not be null.");
		}

		this.metaBuilder = new MetaBuilder(dataSource);
		this.baseModelGenerator = baseModelGenerator;
		this.modelGenerator = modelGenerator;
	}

	public Generator(DataSource dataSource, BaseModelGenerator baseModelGenerator, ModelGenerator modelGenerator, ModelExampleGenerator modelExampleGenerator) {
		if (dataSource == null) {
			throw new IllegalArgumentException("dataSource can not be null.");
		}
		if (baseModelGenerator == null) {
			throw new IllegalArgumentException("baseModelGenerator can not be null.");
		}
		if (modelGenerator == null) {
			throw new IllegalArgumentException("modelGenerator can not be null.");
		}

		this.metaBuilder = new MetaBuilder(dataSource);
		this.baseModelGenerator = baseModelGenerator;
		this.modelGenerator = modelGenerator;
		this.modelExampleGenerator = modelExampleGenerator;
	}

	public Generator(DataSource dataSource, BaseModelGenerator baseModelGenerator, ModelGenerator modelGenerator, ModelExampleGenerator modelExampleGenerator, XmlMapperGenerator xmlMapperGenerator) {
		this(dataSource, baseModelGenerator, modelGenerator, modelExampleGenerator);
		if (xmlMapperGenerator == null) {
			throw new IllegalArgumentException("xmlMapperGenerator can not be null.");
		}
		this.xmlMapperGenerator = xmlMapperGenerator;
	}

	public Generator(String propertiesFile) {
		this(propertiesFile, null);
	}

	public Generator(String propertiesFile, String path) {
		this(PropUtil.read(propertiesFile), path);
	}

	public Generator(File file, String path) {
		this(PropUtil.read(file), path);
	}

	public Generator(File file) {
		this(file, null);
	}

	public Generator(Prop prop, String projectPath) {
		this(prop.get("jdbc.url"), prop.get("jdbc.username"), prop.get("jdbc.password"), prop.get("genarate.sourceRoot"), prop.get("genarate.daoPackage"), prop.get("genarate.servicePackage"), projectPath);
		String exclude = prop.get("generate.excludeTables");
		if (StrKit.notBlank(exclude)) {
			addExcludedTable(exclude.split(","));
		}
		String prefix = prop.get("generate.removePrefix");
		if (StrKit.notBlank(prefix)) {
			setRemovedTableNamePrefixes(prefix.split(","));
		}
		String include = prop.get("generate.includeTables");
		if (StrKit.notBlank(include)) {
			addIncludedTable(include.split(","));
		}
		String service = prop.get("generate.serviceGenrate", "false");
		if ("false".equals(service)) {
			setGenerateService(false);
		}
	}

	public Generator(Prop prop) {
		this(prop, null);
	}

	public void setMetaBuilder(MetaBuilder metaBuilder) {
		if (metaBuilder != null)
			this.metaBuilder = metaBuilder;
	}

	public void setTypeMapping(TypeMapping typeMapping) {
		this.metaBuilder.setTypeMapping(typeMapping);
	}

	public void setGenerateService(boolean generateService) {
		this.generateService = generateService;
	}

	public void setDialect(Dialect dialect) {
		this.metaBuilder.setDialect(dialect);
	}

	public void setRemovedTableNamePrefixes(String[] removedTableNamePrefixes) {
		this.metaBuilder.setRemovedTableNamePrefixes(removedTableNamePrefixes);
	}

	public void addExcludedTable(String[] excludedTables) {
		this.metaBuilder.addExcludedTable(excludedTables);
	}

	public void addIncludedTable(String[] includedTables) {
		this.metaBuilder.addIncludedTable(includedTables);
	}

	public void setGenerateDaoInModel(boolean generateDaoInModel) {
		if (this.modelGenerator != null) {
			this.modelGenerator.setGenerateDaoInModel(generateDaoInModel);
		}
	}

	public void generate() {
		long start = System.currentTimeMillis();
		List<TableMeta> tableMetas = this.metaBuilder.build();
		if (tableMetas.size() == 0) {
			System.out.println("TableMeta 数量为 0，不生成任何文件");
			return;
		}

		this.baseModelGenerator.generate(tableMetas);

		if (this.modelGenerator != null) {
			this.modelGenerator.generate(tableMetas);
		}

		if (this.modelExampleGenerator != null) {
			this.modelExampleGenerator.generate(tableMetas);
		}

		if (this.xmlMapperGenerator != null) {
			this.xmlMapperGenerator.generate(tableMetas);
		}

		if ((this.generateService) && (this.serviceGenerator != null)) {
			this.serviceGenerator.generate(tableMetas);
		}

		long usedTime = (System.currentTimeMillis() - start) / 1000L;
		System.out.println(String.format("结束生成模型层dao，耗时 %s 秒.", usedTime));
	}
}