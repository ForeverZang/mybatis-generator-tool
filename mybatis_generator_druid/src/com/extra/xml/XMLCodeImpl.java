package com.extra.xml;

import java.io.File;
import java.io.FileOutputStream;

public class XMLCodeImpl extends XMLCodeAbstract {
	public static final String SEPERATOR_1 = "\t";
	public static final String SEPERATOR_2 = "\n";
	public static final String HEADER_1 = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
	public static final String HEADER_2 = "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >";
	public String nameSpace;
	public String extendCode;
	public String newBeanPackage;
	public String resultMapCode;
	private boolean hasInit = false;

	public XMLCodeImpl(String sourceRoot, String daoPackage, String beanName, String mapperClass) {
		this.sourceRoot = sourceRoot;
		this.daoPackage = daoPackage;
		this.beanName = beanName;
		this.mapperClass = mapperClass;
	}

	public void init() {
		this.nameSpace = (this.daoPackage + "Ex." + this.mapperClass + "Ex");
		this.extendCode = (this.daoPackage + "." + this.mapperClass + ".BaseResultMap");
		this.newBeanPackage = (this.daoPackage + "Ex.model." + this.beanName + "Ex");
		this.savePath = ("./" + this.sourceRoot + "/" + this.daoPackage.replaceAll("\\.", "/") + "Ex/xml");
		this.resultMapCode = ("<mapper namespace=\"" + this.nameSpace + "\" >\n\t" + "<resultMap id=\"BaseResultMap\" type=\"" + this.newBeanPackage + "\" extends=\"" + this.extendCode + "\">\n\t</resultMap>\n</mapper>");
		this.hasInit = true;
	}

	public String toStr() {
		if (this.hasInit){
			return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n" + this.resultMapCode;
		}
		System.err.println("类未初始化");
		return "";
	}

	public void save() throws Exception {
		init();
		File dir = new File(this.savePath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File xml = new File(this.savePath + "/" + this.mapperClass + "Ex.xml");
		if (xml.exists()) {
			return;
		}
		xml.createNewFile();
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(xml);
			fos.write(toStr().getBytes("UTF-8"));
			fos.flush();
		} catch (Exception e) {
			System.out.println("文件创建失败");
			return;
		} finally {
			if (fos != null) {
				fos.close();
				fos = null;
			}
		}
	}
}
