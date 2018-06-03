package com.config.data;

import javax.sql.DataSource;

public abstract interface IDataSourceProvider {
	public abstract DataSource getDataSource();
}