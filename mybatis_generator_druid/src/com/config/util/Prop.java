package com.config.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class Prop {
	private Properties properties = null;
	public static final String ENCODING = "utf-8";

	public Prop(String fileName) {
		this(fileName, "utf-8");
	}

	public Prop(String fileName, String encoding) {
		InputStream inputStream = null;
		try {
			inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
			if (inputStream == null)
				throw new IllegalArgumentException("Properties file not found in classpath: " + fileName);
			this.properties = new Properties();
			this.properties.load(new InputStreamReader(inputStream, encoding));
		} catch (IOException e) {
			throw new RuntimeException("Error loading properties file.", e);
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException localIOException1) {
				}
		}
	}

	public Prop(File file) {
		this(file, "utf-8");
	}

	public Prop(File file, String encoding) {
		if (file == null)
			throw new IllegalArgumentException("File can not be null.");
		if (!file.isFile()) {
			throw new IllegalArgumentException("File not found : " + file.getName());
		}
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			this.properties = new Properties();
			this.properties.load(new InputStreamReader(inputStream, encoding));
		} catch (IOException e) {
			throw new RuntimeException("Error loading properties file.", e);
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException localIOException1) {
				}
		}
	}

	public String get(String key) {
		return this.properties.getProperty(key);
	}

	public String get(String key, String defaultValue) {
		return this.properties.getProperty(key, defaultValue);
	}
}