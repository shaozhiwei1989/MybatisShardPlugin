package com.nicomama.strategy;

public interface DataSourceStrategy {
    /**
     * 返回分库的后缀
     *
     * @return
     */
    String parse(Object[] args);
}
