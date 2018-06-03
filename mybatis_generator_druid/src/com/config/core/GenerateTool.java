package com.config.core;

import java.io.File;

import com.config.util.Prop;
import com.extra.core.ExtraExecutor;

/**
 * @作者	 臧恒
 * @描述	mybatis代码生成工具
 * @使用简介
 * <pre>
 * 		generateDao		生成持久层dao
 * 		generateDaoEx	生成扩展成daoEx
 * 		
 * </pre>
 */
public class GenerateTool {
	
	/**
	 * @generateDao
	 */
	public static void generateDao(String fileName) {
		generateDao(fileName, null);
	}

	public static void generateDao(String fileName, String path) {
		new Generator(fileName, null).generate();
	}

	public static void generateDao(File file) {
		generateDao(file, null);
	}

	public static void generateDao(File file, String path) {
		new Generator(new Prop(file, "utf-8"), path).generate();
	}
	
	
	
	/**
	 * @generateDaoEx
	 */
	public static void generateDaoEx(String fileName) {
		generateDaoEx(fileName, null);
	}

	public static void generateDaoEx(String fileName, String path) {
		new ExtraExecutor(fileName, null).execute();
	}

	public static void generateDaoEx(File file) {
		generateDaoEx(file, null);
	}

	public static void generateDaoEx(File file, String path) {
		new ExtraExecutor(new Prop(file, "utf-8"), path).execute();
	}

}