package com.yc.framework.beans.config;

/**
 * Created by Tom on 2019/4/13.
 */
public class YCBeanPostProcessor {

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }
}
