package com.yc.framework.aop;

import com.yc.framework.aop.intercept.YCMethodInvocation;
import com.yc.framework.aop.support.YCAdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Created by Tom on 2019/4/14.
 */
public class YCJdkDynamicAopProxy implements YCAopProxy,InvocationHandler{

    private YCAdvisedSupport advised;

    public YCJdkDynamicAopProxy(YCAdvisedSupport config){
        this.advised = config;
    }

    @Override
    public Object getProxy() {
        return getProxy(this.advised.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader,this.advised.getTargetClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Object> interceptorsAndDynamicMethodMatchers = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method,this.advised.getTargetClass());
        YCMethodInvocation invocation = new YCMethodInvocation(proxy,this.advised.getTarget(),method,args,this.advised.getTargetClass(),interceptorsAndDynamicMethodMatchers);
        return invocation.proceed();
    }
}
