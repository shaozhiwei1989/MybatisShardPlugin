package com.nicomama.parser;

import org.apache.ibatis.mapping.BoundSql;

import java.lang.reflect.Field;

public class BoundSqlParser {
    private BoundSql boundSql;
    private static Field fsql;

    static {
        try {
            fsql = BoundSql.class.getDeclaredField("sql");
            fsql.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private BoundSqlParser(BoundSql boundSql) {
        this.boundSql = boundSql;
    }

    public static BoundSqlParser bind(BoundSql boundSql) {
        return new BoundSqlParser(boundSql);
    }

    public void changeSql(String sql) {
        try {
            fsql.set(boundSql, sql);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
