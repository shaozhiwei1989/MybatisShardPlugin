package com.nicomama;

import com.nicomama.parser.SqlParam;
import com.nicomama.strategy.ShardStrategy;

public class TypeShardStrategy implements ShardStrategy {

    @Override
    public String parse(SqlParam sqlParam) {
        Long id = (Long) sqlParam.get("id");
        return "_" + id % 3;//返回分表的后缀
    }

}
