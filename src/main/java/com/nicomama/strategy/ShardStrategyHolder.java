package com.nicomama.strategy;

import java.util.HashMap;
import java.util.Map;

public class ShardStrategyHolder {
    private static final ShardStrategyHolder INSTANCE = new ShardStrategyHolder();
    private final Map<String, ShardStrategy> shardStrategy = new HashMap<>();

    private ShardStrategyHolder() {

    }

    public static ShardStrategyHolder getInstance() {
        return INSTANCE;
    }

    public void add(String tableName, ShardStrategy strategy) {
        shardStrategy.put(tableName, strategy);
    }

    public ShardStrategy get(String tableName) {
        return shardStrategy.get(tableName);
    }

}
