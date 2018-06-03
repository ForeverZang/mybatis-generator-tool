package com.config.util;

import java.io.File;

public class PropUtil {
	public static Prop read(String fileName) {
		return read(fileName, "utf-8");
	}

	public static Prop read(String fileName, String encoding) {
		Prop result = new Prop(fileName, encoding);
		return result;
	}

	public static Prop read(File file) {
		return read(file, "utf-8");
	}

	public static Prop read(File file, String encoding) {
		Prop result = new Prop(file, encoding);
		return result;
	}
}