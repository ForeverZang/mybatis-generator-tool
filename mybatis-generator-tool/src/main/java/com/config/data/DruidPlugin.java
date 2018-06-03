package com.config.data;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.config.util.StrKit;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class DruidPlugin implements IPlugin, IDataSourceProvider {
	private String url;
	private String username;
	private String password;
	private String driverClass = null;

	private int initialSize = 10;
	private int minIdle = 10;
	private int maxActive = 100;

	private long maxWait = -1L;

	private long timeBetweenEvictionRunsMillis = 60000L;

	private long minEvictableIdleTimeMillis = 1800000L;

	private long timeBetweenConnectErrorMillis = 30000L;

	private String validationQuery = "select 1";
	private boolean testWhileIdle = true;
	private boolean testOnBorrow = false;
	private boolean testOnReturn = false;

	private boolean removeAbandoned = false;

	private long removeAbandonedTimeoutMillis = 300000L;

	private boolean logAbandoned = false;

	private int maxPoolPreparedStatementPerConnectionSize = -1;
	private String filters;
	private List<Filter> filterList;
	private DruidDataSource ds;
	private boolean isStarted = false;

	public DruidPlugin(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}

	public DruidPlugin(String url, String username, String password, String driverClass) {
		this.url = url;
		this.username = username;
		this.password = password;
		this.driverClass = driverClass;
	}

	public DruidPlugin(String url, String username, String password, String driverClass, String filters) {
		this.url = url;
		this.username = username;
		this.password = password;
		this.driverClass = driverClass;
		this.filters = filters;
	}

	public DruidPlugin setFilters(String filters) {
		this.filters = filters;
		return this;
	}

	public synchronized DruidPlugin addFilter(Filter filter) {
		if (this.filterList == null)
			this.filterList = new ArrayList<Filter>();
		this.filterList.add(filter);
		return this;
	}

	public DataSource getDataSource() {
		return this.ds;
	}

	public boolean start() {
		if (this.isStarted) {
			return true;
		}
		this.ds = new DruidDataSource();

		this.ds.setUrl(this.url);
		this.ds.setUsername(this.username);
		this.ds.setPassword(this.password);
		if (this.driverClass != null)
			this.ds.setDriverClassName(this.driverClass);
		this.ds.setInitialSize(this.initialSize);
		this.ds.setMinIdle(this.minIdle);
		this.ds.setMaxActive(this.maxActive);
		this.ds.setMaxWait(this.maxWait);
		this.ds.setTimeBetweenConnectErrorMillis(this.timeBetweenConnectErrorMillis);
		this.ds.setTimeBetweenEvictionRunsMillis(this.timeBetweenEvictionRunsMillis);
		this.ds.setMinEvictableIdleTimeMillis(this.minEvictableIdleTimeMillis);

		this.ds.setValidationQuery(this.validationQuery);
		this.ds.setTestWhileIdle(this.testWhileIdle);
		this.ds.setTestOnBorrow(this.testOnBorrow);
		this.ds.setTestOnReturn(this.testOnReturn);

		this.ds.setRemoveAbandoned(this.removeAbandoned);
		this.ds.setRemoveAbandonedTimeoutMillis(this.removeAbandonedTimeoutMillis);
		this.ds.setLogAbandoned(this.logAbandoned);

		this.ds.setMaxPoolPreparedStatementPerConnectionSize(this.maxPoolPreparedStatementPerConnectionSize);

		if (StrKit.notBlank(this.filters)) {
			try {
				this.ds.setFilters(this.filters);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		addFilterList(this.ds);

		this.isStarted = true;
		return true;
	}

	private void addFilterList(DruidDataSource ds) {
		if (this.filterList != null) {
			List<Filter> targetList = ds.getProxyFilters();
			for (Filter add : this.filterList) {
				boolean found = false;
				for (Filter target : targetList) {
					if (add.getClass().equals(target.getClass())) {
						found = true;
						break;
					}
				}
				if (!found)
					targetList.add(add);
			}
		}
	}

	public boolean stop() {
		if (this.ds != null) {
			this.ds.close();
		}
		this.ds = null;
		this.isStarted = false;
		return true;
	}

	public DruidPlugin set(int initialSize, int minIdle, int maxActive) {
		this.initialSize = initialSize;
		this.minIdle = minIdle;
		this.maxActive = maxActive;
		return this;
	}

	public DruidPlugin setDriverClass(String driverClass) {
		this.driverClass = driverClass;
		return this;
	}

	public DruidPlugin setInitialSize(int initialSize) {
		this.initialSize = initialSize;
		return this;
	}

	public DruidPlugin setMinIdle(int minIdle) {
		this.minIdle = minIdle;
		return this;
	}

	public DruidPlugin setMaxActive(int maxActive) {
		this.maxActive = maxActive;
		return this;
	}

	public DruidPlugin setMaxWait(long maxWait) {
		this.maxWait = maxWait;
		return this;
	}

	public DruidPlugin setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
		return this;
	}

	public DruidPlugin setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
		return this;
	}

	public DruidPlugin setValidationQuery(String validationQuery) {
		this.validationQuery = validationQuery;
		return this;
	}

	public DruidPlugin setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
		return this;
	}

	public DruidPlugin setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
		return this;
	}

	public DruidPlugin setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
		return this;
	}

	public DruidPlugin setMaxPoolPreparedStatementPerConnectionSize(int maxPoolPreparedStatementPerConnectionSize) {
		this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
		return this;
	}

	public final void setTimeBetweenConnectErrorMillis(long timeBetweenConnectErrorMillis) {
		this.timeBetweenConnectErrorMillis = timeBetweenConnectErrorMillis;
	}

	public final void setRemoveAbandoned(boolean removeAbandoned) {
		this.removeAbandoned = removeAbandoned;
	}

	public final void setRemoveAbandonedTimeoutMillis(long removeAbandonedTimeoutMillis) {
		this.removeAbandonedTimeoutMillis = removeAbandonedTimeoutMillis;
	}

	public final void setLogAbandoned(boolean logAbandoned) {
		this.logAbandoned = logAbandoned;
	}
}