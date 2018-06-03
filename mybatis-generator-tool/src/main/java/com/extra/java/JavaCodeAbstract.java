package com.extra.java;

public abstract class JavaCodeAbstract {

	public boolean isMoedl() {
		return isMoedl;
	}

	public void setMoedl(boolean isMoedl) {
		this.isMoedl = isMoedl;
	}

	public String getSourceRoot() {
		return sourceRoot;
	}

	public void setSourceRoot(String sourceRoot) {
		this.sourceRoot = sourceRoot;
	}

	public String getDaoPackage() {
		return daoPackage;
	}

	public void setDaoPackage(String daoPackage) {
		this.daoPackage = daoPackage;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	public abstract void save() throws Exception;

	public abstract String toStr();

	protected String sourceRoot;
	protected String daoPackage;
	protected String beanName;
	protected boolean isMoedl;
}
