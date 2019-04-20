package com.yc.framework.aop.aspect;

import com.yc.framework.aop.intercept.YCMethodInterceptor;
import com.yc.framework.aop.intercept.YCMethodInvocation;

import java.lang.reflect.Method;

/**
 * Created by Tom on 2019/4/15.
 */
public class YCAfterThrowingAdviceInterceptor extends YCAbstractAspectAdvice implements YCAdvice,YCMethodInterceptor {


    private String throwingName;

    public YCAfterThrowingAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(YCMethodInvocation mi) throws Throwable {
        try {
            return mi.proceed();
        }catch (Throwable e){
            invokeAdviceMethod(mi,null,e.getCause());
            throw e;
        }
    }

    public void setThrowName(String throwName){
        this.throwingName = throwName;
    }
}
