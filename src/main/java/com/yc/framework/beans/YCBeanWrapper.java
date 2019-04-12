package com.yc.framework.beans;

/**
 * @author Yanchen
 * @ClassName YCBeanWrapper
 * @Date 2019/4/11 10:14
 */
public class YCBeanWrapper {
    private Object wrappedInstance;
    private Class<?> wrappedClass;

    public YCBeanWrapper(Object wrappedInstance){
        this.wrappedInstance = wrappedInstance;
    }

    public Object getWrappedInstance(){
        return this.wrappedInstance;
    }

    // 返回代理以后的Class
    // 可能会是这个 $Proxy0
    public Class<?> getWrappedClass(){
        return this.wrappedInstance.getClass();
    }
}
