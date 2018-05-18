package com.nicomama.datasource;

public class DataSourceHolder {
    static final ThreadLocal<String> dataSourceName = new ThreadLocal<>();

    public static void set(String dataSourceName) {
        DataSourceHolder.dataSourceName.set(dataSourceName);
    }

    public static String get() {
        return dataSourceName.get();
    }

    public static void remove() {
        dataSourceName.remove();
    }


}
