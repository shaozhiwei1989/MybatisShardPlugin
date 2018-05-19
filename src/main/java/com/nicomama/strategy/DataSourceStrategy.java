package com.nicomama.strategy;

import com.nicomama.parser.DSParam;

public interface DataSourceStrategy {
    /**
     * 返回数据源名称
     *
     * @return
     */
    String parse(DSParam dsParam);
}
