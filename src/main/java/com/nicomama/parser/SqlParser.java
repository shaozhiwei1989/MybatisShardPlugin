package com.nicomama.parser;

import com.nicomama.strategy.ShardStrategy;
import com.nicomama.strategy.ShardStrategyHolder;
import org.apache.ibatis.mapping.BoundSql;

import java.util.ArrayList;
import java.util.List;

public class SqlParser {
    private List<String> tableNames = new ArrayList<>();
    private BoundSql boundSql;

    public SqlParser(BoundSql boundSql) {
        this.boundSql = boundSql;
        findTableName(boundSql.getSql());
    }


    public String toSql() {
        String sql = boundSql.getSql();
        for (String tableName : tableNames) {
            ShardStrategy strategy = ShardStrategyHolder.getInstance().get(tableName);
            if (strategy == null) {
                continue;
            }
            String suffix = strategy.parse(new SqlParam(boundSql));
            sql = sql.replace("##" + tableName + "##", tableName + suffix);
        }
        return sql;
    }


    public boolean isShardingSql() {
        return !tableNames.isEmpty();
    }

    private void findTableName(String sql) {
        int beginIndex = sql.indexOf("##");
        if (beginIndex == -1) {
            return;
        }
        int endIndex = 0;
        for (int i = beginIndex + 2; ; i++) {
            if (i >= sql.length() || (sql.charAt(i) == '#' && sql.charAt(i + 1) == '#')) {
                endIndex = i;
                break;
            }
        }
        //select * from ##user
        //只有前面##未找到后面匹配##认为不是标准的分表sql，不做处理
        if (endIndex >= sql.length()) {
            return;
        }
        tableNames.add(sql.substring(beginIndex + 2, endIndex));
        findTableName(sql.substring(endIndex + 2, sql.length()));
    }

}
