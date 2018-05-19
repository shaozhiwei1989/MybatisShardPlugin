package com.nicomama.spring;

import com.nicomama.annotation.DataSource;
import com.nicomama.datasource.DataSourceHolder;
import com.nicomama.parser.DSParam;
import com.nicomama.strategy.DataSourceStrategy;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class Interceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        DataSource dataSource = invocation.getMethod().getAnnotation(DataSource.class);
        if (dataSource == null) {
            return invocation.proceed();
        }
        try {
            String dataSourceName = getDataSoureName(dataSource, invocation);
            DataSourceHolder.set(dataSourceName);
            return invocation.proceed();
        } finally {
            DataSourceHolder.remove();
        }
    }


    private String getDataSoureName(DataSource dataSource, MethodInvocation invocation) throws Exception {
        if (dataSource.value() != null && dataSource.value().length() > 0) {
            return dataSource.value();
        }
        Class<?> clazz = dataSource.strategy();
        DataSourceStrategy strategy = (DataSourceStrategy) clazz.newInstance();
        return strategy.parse(new DSParam(invocation));
    }
}
