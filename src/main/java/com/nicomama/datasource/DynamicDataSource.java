package com.nicomama.datasource;

import com.nicomama.exception.NoDataSourceException;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Map;
import java.util.logging.Logger;

public class DynamicDataSource implements DataSource {
    private Map<String, DataSource> dataSourceMap;
    private DataSource defaultDataSource;


    @Override
    public Connection getConnection() throws SQLException {
        return getCurrentDataSource().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getCurrentDataSource().getConnection(username, password);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return getCurrentDataSource().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return getCurrentDataSource().isWrapperFor(iface);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return getCurrentDataSource().getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return getCurrentDataSource().getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return getCurrentDataSource().getParentLogger();
    }

    public Map<String, DataSource> getDataSourceMap() {
        return dataSourceMap;
    }

    public void setDataSourceMap(Map<String, DataSource> dataSourceMap) {
        this.dataSourceMap = dataSourceMap;
    }

    public DataSource getDefaultDataSource() {
        return defaultDataSource;
    }

    public void setDefaultDataSource(DataSource defaultDataSource) {
        this.defaultDataSource = defaultDataSource;
    }


    public DynamicDataSource addDataSource(String dataSourceName, DataSource dataSource) {
        this.dataSourceMap.put(dataSourceName, dataSource);
        return this;
    }

    public DynamicDataSource removeDataSource(String dataSourceName) {
        this.dataSourceMap.remove(dataSourceName);
        return this;
    }

    public DynamicDataSource clearDataSource() {
        this.dataSourceMap.clear();
        return this;
    }

    private DataSource getCurrentDataSource() {
        if ((dataSourceMap == null || dataSourceMap.isEmpty()) && defaultDataSource == null) {
            throw new NoDataSourceException("at least config one data source.");
        }
        String dataSourceName = DataSourceHolder.getDataSourceName();
        DataSource dataSource = dataSourceMap.get(dataSourceName);
        if (dataSource == null) {
            dataSource = defaultDataSource;
        }
        return dataSource;
    }

}
