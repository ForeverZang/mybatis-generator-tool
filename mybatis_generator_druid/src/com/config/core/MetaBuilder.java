package com.config.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.sql.DataSource;

import com.config.dialect.Dialect;
import com.config.dialect.MysqlDialect;
import com.config.dialect.OracleDialect;
import com.config.util.StrKit;

public class MetaBuilder {
	private DataSource dataSource;
	protected Dialect dialect = new MysqlDialect();

	protected Set<String> excludedTables = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

	protected Set<String> includedTables = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);

	protected Connection conn = null;
	protected DatabaseMetaData dbMeta = null;

	protected String[] removedTableNamePrefixes = null;

	protected TypeMapping typeMapping = new TypeMapping();

	public MetaBuilder(DataSource dataSource) {
		if (dataSource == null) {
			throw new IllegalArgumentException("dataSource can not be null.");
		}
		this.dataSource = dataSource;
	}

	public void setDialect(Dialect dialect) {
		if (dialect != null)
			this.dialect = dialect;
	}

	public void addExcludedTable(String[] excludedTables) {
		if (excludedTables != null)
			for (String table : excludedTables)
				if (StrKit.notBlank(table))
					this.excludedTables.add(table);
	}

	public void addIncludedTable(String[] includedTables) {
		if (includedTables != null)
			for (String table : includedTables)
				if (StrKit.notBlank(table))
					this.includedTables.add(table);
	}

	public void setRemovedTableNamePrefixes(String[] removedTableNamePrefixes) {
		this.removedTableNamePrefixes = removedTableNamePrefixes;
	}

	public void setTypeMapping(TypeMapping typeMapping) {
		if (typeMapping != null)
			this.typeMapping = typeMapping;
	}

	public List<TableMeta> build() {
		System.out.println("开始构建 TableMeta ...");
		try {
			this.conn = this.dataSource.getConnection();
			this.dbMeta = this.conn.getMetaData();

			List<TableMeta> ret = new ArrayList<TableMeta>();
			buildTableNames(ret);
			for (TableMeta tableMeta : ret) {
				buildPrimaryKey(tableMeta);
				buildColumnMetas(tableMeta);
			}
			return ret;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (this.conn != null)
				try {
					this.conn.close();
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}

		}
	}

	protected boolean isSkipTable(String tableName) {
		return false;
	}

	protected boolean startWithPrefix(String tableName) {
		if (this.removedTableNamePrefixes != null) {
			for (String prefix : this.removedTableNamePrefixes) {
				if (tableName.startsWith(prefix)) {
					return true;
				}
			}
			return false;
		}
		return true;
	}

	protected String buildModelName(String tableName) {
		if (this.removedTableNamePrefixes != null) {
			for (String prefix : this.removedTableNamePrefixes) {
				if (tableName.startsWith(prefix)) {
					tableName = tableName.replaceFirst(prefix, "");
					break;
				}
			}

		}

		if ((this.dialect instanceof OracleDialect)) {
			tableName = tableName.toLowerCase();
		}

		return StrKit.firstCharToUpperCase(StrKit.toCamelCase(tableName));
	}

	protected String buildBaseModelName(String modelName) {
		return modelName;
	}

	protected ResultSet getTablesResultSet() throws SQLException {
		String schemaPattern = (this.dialect instanceof OracleDialect) ? this.dbMeta.getUserName() : null;
		return this.dbMeta.getTables(this.conn.getCatalog(), schemaPattern, null, new String[] { "TABLE", "VIEW" });
	}

	protected void buildTableNames(List<TableMeta> ret) throws SQLException {
		ResultSet rs = getTablesResultSet();
		while (rs.next()) {
			String tableName = rs.getString("TABLE_NAME");

			if (this.excludedTables.contains(tableName)) {
				System.out.println("忽略表 :" + tableName);
			} else if (isSkipTable(tableName)) {
				System.out.println("忽略表  :" + tableName);
			} else if (startWithPrefix(tableName)) {
				if ((this.includedTables.size() <= 0) || (this.includedTables.contains(tableName))) {
					TableMeta tableMeta = new TableMeta();
					tableMeta.name = tableName;
					tableMeta.remarks = rs.getString("REMARKS");

					tableMeta.modelName = buildModelName(tableName);
					tableMeta.baseModelName = buildBaseModelName(tableMeta.modelName);
					ret.add(tableMeta);
				}
			}
		}
		rs.close();
	}

	protected void buildPrimaryKey(TableMeta tableMeta) throws SQLException {
		ResultSet rs = this.dbMeta.getPrimaryKeys(this.conn.getCatalog(), null, tableMeta.name);

		String primaryKey = "";
		String primaryKeyType = "";
		int index = 0;
		while (rs.next()) {
			if (index++ > 0) {
				primaryKey = primaryKey + ",";
				primaryKeyType = primaryKeyType + ",";
			}
			primaryKey = primaryKey + rs.getString("COLUMN_NAME");
			primaryKeyType = primaryKeyType + this.typeMapping.getType(rs.getMetaData().getColumnClassName(index));
		}
		tableMeta.primaryKey = primaryKey;
		tableMeta.primaryKeyJavaType = primaryKeyType;
		tableMeta.len = index;
		rs.close();
	}

	protected void buildColumnMetas(TableMeta tableMeta) throws SQLException {
		String sql = this.dialect.forTableBuilderDoBuild(tableMeta.name);
		Statement stm = this.conn.createStatement();
		ResultSet rs = stm.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();

		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			ColumnMeta cm = new ColumnMeta();
			cm.name = rsmd.getColumnName(i);
			String colClassName = rsmd.getColumnClassName(i);
			String typeStr = this.typeMapping.getType(colClassName);
			int type = rsmd.getColumnType(i);
			cm.jdbcType = type;
			if (typeStr != null) {
				cm.javaType = typeStr;
			} else if ((type == -2) || (type == -3) || (type == 2004)) {
				cm.javaType = "byte[]";
			} else if ((type == 2005) || (type == 2011)) {
				cm.javaType = "java.lang.String";
			} else {
				cm.javaType = "java.lang.String";
			}

			if (type == -1) {
				tableMeta.big += 1;
			}

			cm.attrName = buildAttrName(cm.name);
			cm.isPrimaryKey = isPrimaryKey(cm.name, tableMeta.primaryKey);
			tableMeta.columnMetas.add(cm);
		}

		rs.close();
		stm.close();
	}

	protected boolean isPrimaryKey(String attrName, String primaryKey) {
		for (String str : primaryKey.split(",")) {
			if (attrName.equals(str)) {
				return true;
			}
		}
		return false;
	}

	protected String buildAttrName(String colName) {
		if ((this.dialect instanceof OracleDialect)) {
			colName = colName.toLowerCase();
		}
		return StrKit.toCamelCase(colName);
	}
}