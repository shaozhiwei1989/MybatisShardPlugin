package com.nicomama.parser;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.ibatis.annotations.Param;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class DSParam {
    private Map<String, Object> dsParam = new HashMap<>();
    private final static Set<Class<?>> SINGLE_TYPE_PARAM = new HashSet<>();

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

    public DSParam(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        Object[] args = invocation.getArguments();
        if (args == null || args.length == 0) {
            return;
        }
        if (args.length == 1) {
            if (SINGLE_TYPE_PARAM.contains(args[0].getClass())) {
                Parameter parameter = method.getParameters()[0];
                Param param = parameter.getAnnotation(Param.class);
                String fName = (param == null) ? parameter.getName() : param.value();
                dsParam.put(fName, args[0]);
            } else if (args[0] instanceof Collection) {
                Collection collection = (Collection) args[0];
                int i = 0;
                for (Object obj : collection) {
                    dsParam.put(i + "", obj);
                    i++;
                }
            } else if (args[0].getClass().isArray()) {
                Object[] objects = (Object[]) args[0];
                for (int i = 0; i < objects.length; i++) {
                    dsParam.put(i + "", objects[i]);
                }
            } else if (args[0] instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) args[0];
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    String key = entry.getKey() != null ? entry.getKey().toString() : null;
                    dsParam.put(key, entry.getValue());
                }
            } else {
                try {
                    dsParam = objectToMap(args[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            int index = 0;
            for (Parameter parameter : method.getParameters()) {
                Param param = parameter.getAnnotation(Param.class);
                String fName = (param == null) ? parameter.getName() : param.value();
                dsParam.put(fName, args[index++]);
            }
        }
    }

    public Object get(String key) {
        return dsParam.get(key);
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
