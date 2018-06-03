package com.config.data;

public class DataAccount {
	private String jdbcUrl;
	private String jdbcName;
	private String jdbcPassword;

	public DataAccount(String jdbcUrl, String jdbcName, String jdbcPassword) {
		this.jdbcUrl = jdbcUrl;
		this.jdbcName = jdbcName;
		this.jdbcPassword = jdbcPassword;
	}

	public String getJdbcUrl() {
		return this.jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getJdbcName() {
		return this.jdbcName;
	}

	public void setJdbcName(String jdbcName) {
		this.jdbcName = jdbcName;
	}

	public String getJdbcPassword() {
		return this.jdbcPassword;
	}

	public void setJdbcPassword(String jdbcPassword) {
		this.jdbcPassword = jdbcPassword;
	}
}