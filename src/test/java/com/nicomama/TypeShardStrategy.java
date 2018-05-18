package com.nicomama;

import com.nicomama.annotation.DataSource;
import com.nicomama.parser.SqlParam;
import com.nicomama.strategy.ShardStrategy;

public class TypeShardStrategy implements ShardStrategy {

    @DataSource(name = "abcd")
    @Override
    public String parse(SqlParam sqlParam) {
      //  Long id = (Long) sqlParam.get("id");
        return "_" + 5 % 3;//返回分表的后缀
    }

    public static void main(String[] args) {
        ShardStrategy strategy = new TypeShardStrategy();
      String strs=  strategy.parse(null);
        System.out.printf(strs+"\n");
    }

}
