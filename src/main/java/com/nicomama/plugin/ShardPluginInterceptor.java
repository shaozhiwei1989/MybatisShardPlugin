package com.nicomama.plugin;


import com.nicomama.parser.BoundSqlParser;
import com.nicomama.parser.SqlParser;
import com.nicomama.parser.XmlParser;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.util.Properties;

@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class ShardPluginInterceptor implements Interceptor {


    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        SqlParser sqlParser = new SqlParser(boundSql);
        if (sqlParser.isShardingSql()) {
            String newSql = sqlParser.toSql();
            BoundSqlParser.bind(boundSql).changeSql(newSql);
        }
        return invocation.proceed();
    }

    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    public void setProperties(Properties properties) {
        String configLocation = properties.getProperty("configLocation");
        if (configLocation == null) {
            throw new IllegalArgumentException("ShardPluginInterceptor[" + getClass().getName() + "] " + "Property[configLocation] Cannot Empty");
        }
        XmlParser.parse(configLocation);
    }
}
