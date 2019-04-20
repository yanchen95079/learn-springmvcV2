package com.yc.framework.aop;

/**
 * Created by Tom.
 */
public interface YCAopProxy {


    Object getProxy();


    Object getProxy(ClassLoader classLoader);
}
