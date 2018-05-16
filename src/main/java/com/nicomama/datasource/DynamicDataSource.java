package com.nicomama.datasource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

public class DynamicDataSource implements DataSource {
    private DataSourceWrapper[] dataSources;
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


    public void setDataSources(DataSourceWrapper[] dataSources) {
        this.dataSources = dataSources;
    }

    private DataSource getCurrentDataSource() {
        String dataSourceName = DataSourceHolder.getDataSourceName();
        System.out.printf("dataSourceName:"+dataSourceName+"\n");
        for (DataSourceWrapper dataSource : dataSources) {
            if (dataSource.getDataSourceName().equals(dataSourceName)) {
                return dataSource.getDataSource();
            }
        }
        return defaultDataSource;
    }


    public void init() {
        for (DataSourceWrapper dataSource : dataSources) {
            if (dataSource.isDefault()) {
                defaultDataSource = dataSource.getDataSource();
            }
        }
        defaultDataSource = dataSources[0].getDataSource();
    }
}
