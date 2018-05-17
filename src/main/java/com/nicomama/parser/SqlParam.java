package com.nicomama.parser;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.ParameterMapping;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.*;

public class SqlParam {
    private final static Set<Class<?>> SINGLE_TYPE_PARAM = new HashSet<>();
    private Map<String, Object> sqlParam = new HashMap<>();
    private BoundSql boundSql;

    static {
        SINGLE_TYPE_PARAM.add(int.class);
        SINGLE_TYPE_PARAM.add(Integer.class);

        SINGLE_TYPE_PARAM.add(long.class);
        SINGLE_TYPE_PARAM.add(Long.class);

        SINGLE_TYPE_PARAM.add(short.class);
        SINGLE_TYPE_PARAM.add(Short.class);

        SINGLE_TYPE_PARAM.add(byte.class);
        SINGLE_TYPE_PARAM.add(Byte.class);

        SINGLE_TYPE_PARAM.add(float.class);
        SINGLE_TYPE_PARAM.add(Float.class);

        SINGLE_TYPE_PARAM.add(double.class);
        SINGLE_TYPE_PARAM.add(Double.class);

        SINGLE_TYPE_PARAM.add(boolean.class);
        SINGLE_TYPE_PARAM.add(Boolean.class);

        SINGLE_TYPE_PARAM.add(char.class);
        SINGLE_TYPE_PARAM.add(Character.class);

        SINGLE_TYPE_PARAM.add(String.class);
    }


    SqlParam(BoundSql boundSql) {
        Object parameter = boundSql.getParameterObject();
        if (SINGLE_TYPE_PARAM.contains(parameter.getClass())) {
            List<ParameterMapping> mappings = boundSql.getParameterMappings();
            if (mappings != null) {
                for (ParameterMapping mapping : mappings) {
                    sqlParam.put(mapping.getProperty(), parameter);
                }
            }
        } else if (parameter instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) parameter;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String key = entry.getKey() != null ? entry.getKey().toString() : null;
                sqlParam.put(key, entry.getValue());
            }
        } else if (parameter instanceof Collection) {
            Collection collection = (Collection) parameter;
            int i = 0;
            for (Object obj : collection) {
                sqlParam.put(i + "", obj);
                i++;
            }
        } else if (parameter.getClass().isArray()) {
            Object[] objects = (Object[]) parameter;
            for (int i = 0; i < objects.length; i++) {
                sqlParam.put(i + "", objects[i]);
            }
        } else {
            try {
                sqlParam = objectToMap(parameter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.boundSql = boundSql;
    }

    public Object get(String key) {
        return sqlParam.get(key);
    }

    public Class<?> getParamType() {
        return boundSql.getParameterObject().getClass();
    }


    private Map<String, Object> objectToMap(Object obj) throws Exception {
        Map<String, Object> objectMap = new HashMap<>();
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        if (pds != null && pds.length > 0) {
            for (PropertyDescriptor pd : pds) {
                Object val = pd.getReadMethod().invoke(obj);
                objectMap.put(pd.getName(), val);
            }
        }
        return objectMap;
    }

}
