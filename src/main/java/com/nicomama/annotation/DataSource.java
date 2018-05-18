package com.nicomama.annotation;

import com.nicomama.strategy.DataSourceStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DataSource {
    String value() default "";

    Class<?> strategy() default DataSourceStrategy.class;
}
