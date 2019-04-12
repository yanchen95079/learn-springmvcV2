package com.yc.framework.beans.support;

import com.yc.framework.beans.config.YCBeanDefinition;
import com.yc.framework.context.support.YCAbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yanchen
 * @ClassName YCDefaultListableBeanFactory
 * @Date 2019/4/11 10:21
 */
public class YCDefaultListableBeanFactory extends YCAbstractApplicationContext {
    //存储注册信息的 BeanDefinition
    protected final Map<String, YCBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String,
                YCBeanDefinition>();
}
