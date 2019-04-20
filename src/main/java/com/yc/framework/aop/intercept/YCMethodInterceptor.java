package com.yc.framework.aop.intercept;

/**
 * Created by Tom on 2019/4/14.
 */
public interface YCMethodInterceptor {
    Object invoke(YCMethodInvocation invocation) throws Throwable;
}
