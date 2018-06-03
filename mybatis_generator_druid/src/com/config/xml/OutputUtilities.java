package com.config.xml;

public class OutputUtilities {
	private static String lineSeparator = null;

	static {
		lineSeparator = System.getProperty("line.separator");
		if (lineSeparator == null) {
			lineSeparator = "\n";
		}
	}

	public static void xmlIndent(StringBuilder sb, int indentLevel) {
		for (int i = 0; i < indentLevel; i++)
			sb.append("    ");
	}

	public static void newLine(StringBuilder sb) {
		sb.append(lineSeparator);
	}
}