package com.nicomama.strategy;

import com.nicomama.parser.SqlParam;

public interface ShardStrategy {

    /**
     * 返回分表的后缀
     *
     * @param sqlParam
     * @return
     */
    String parse(SqlParam sqlParam);

}
