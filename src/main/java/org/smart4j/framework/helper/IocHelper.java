package org.smart4j.framework.helper;

import org.apache.commons.lang3.ArrayUtils;
import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by hjy on 15-12-30.
 * 依赖注入助手类
 */
public final class IocHelper {

    static {
        //获取所有的Bean类与Bean实例之间的映射关系（简称Bean Map）
        Map<Class<?>,Object> beanMap = BeanHelper.getBeanMap();
        if (CollectionUtil.isNotEmpty(beanMap)){
//            遍历Bean Map
            for (Map.Entry<Class<?>,Object> beanEntry : beanMap.entrySet()){
//                从BeanMap 中获取Bean类与Bean实例
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
//                获取Bean类定义的所有成员变量（简称Bean Field）
                Field[] beanFileds = beanClass.getDeclaredFields();
                if(ArrayUtils.isNotEmpty(beanFileds)){
//                    遍历Bean Field
                    for (Field beanField:beanFileds){
//                        判断当前Bean Field 是否带有Inject 注解
                        if (beanField.isAnnotationPresent(Inject.class)){
//                            在Bean Map　中获取 Bean Field 对应的实例
                            Class<?> beanFieldClass = beanField.getType();
                            Object beanFieldInstance = beanMap.get(beanFieldClass);
                            if (beanFieldInstance !=null){
//                                通过反射初始化　BeanField的值
                                ReflectionUtil.setField(beanFieldInstance,beanField,beanFieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }
}
