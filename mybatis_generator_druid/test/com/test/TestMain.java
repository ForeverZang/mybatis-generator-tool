package com.test;

import com.config.core.GenerateTool;

public class TestMain {

	public static void main(String[] args) {
		String path = TestMain.class.getResource("/").getPath();
		System.out.println("=======path:" + path);
		
		
		GenerateTool.generateDao("generator.properties", path);
		
		
		//GenerateTool.generateDaoEx("generator.properties", path);
	}
	
}
