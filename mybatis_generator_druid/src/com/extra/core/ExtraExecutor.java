package com.extra.core;

import com.config.util.Prop;
import com.config.util.PropUtil;
import com.extra.java.JavaCodeImpl;
import com.extra.xml.XMLCodeImpl;

public class ExtraExecutor {

	private Prop prop;
	private String projectPath;

	public ExtraExecutor(String fileName, String path) {
		this.prop = PropUtil.read(fileName);
		this.projectPath = path;
	}

	public ExtraExecutor(Prop prop, String path) {
		this.prop = prop;
		this.projectPath = path;
	}

	public void execute() {
		String sourceRoot = prop.get("genarate.sourceRoot");
		if (null == sourceRoot || sourceRoot.trim().length() == 0) {
			System.err.println("sourceRoot不能为空！");
			return;
		}
		String daoPackage = prop.get("genarate.daoEx.daoPackage");
		if (null == daoPackage || daoPackage.trim().length() == 0) {
			System.err.println("daoPackage不能为空！");
			return;
		}
		String beanName = prop.get("genarate.daoEx.beanName");
		if (null == beanName || beanName.trim().length() == 0) {
			System.err.println("beanName不能为空！");
			return;
		}
		
		System.out.println("开始生成扩展层daoEx:" + beanName);

		String mapperName = beanName + "Mapper";
		{
			XMLCodeImpl xai = new XMLCodeImpl(sourceRoot, daoPackage, beanName, mapperName);
			try {
				xai.save();
				System.out.println("扩展层daoEx.xml." + mapperName + "Ex生成成功！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		{
			JavaCodeImpl cai = new JavaCodeImpl(sourceRoot, daoPackage, beanName, true);
			try {
				cai.save();
				System.out.println("扩展层daoEx.model." + beanName + "Ex生成成功！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		{
			JavaCodeImpl cai = new JavaCodeImpl(sourceRoot, daoPackage, mapperName, false);
			try {
				cai.save();
				System.out.println("扩展层daoEx.model." + beanName + "Ex生成成功！");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("结束生成扩展层daoEx:" + beanName);
	}

	String getProjectPath() {
		return this.projectPath == null ? System.getProperty("user.dir") : this.projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

}
