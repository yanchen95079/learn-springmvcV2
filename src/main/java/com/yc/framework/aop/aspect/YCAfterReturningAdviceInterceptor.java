package com.yc.framework.aop.aspect;

import com.yc.framework.aop.intercept.YCMethodInterceptor;
import com.yc.framework.aop.intercept.YCMethodInvocation;

import java.lang.reflect.Method;

/**
 * Created by Tom on 2019/4/15.
 */
public class YCAfterReturningAdviceInterceptor extends YCAbstractAspectAdvice implements YCAdvice,YCMethodInterceptor {

    private YCJoinPoint joinPoint;

    public YCAfterReturningAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(YCMethodInvocation mi) throws Throwable {
        Object retVal = mi.proceed();
        this.joinPoint = mi;
        this.afterReturning(retVal,mi.getMethod(),mi.getArguments(),mi.getThis());
        return retVal;
    }

    private void afterReturning(Object retVal, Method method, Object[] arguments, Object aThis) throws Throwable {
        super.invokeAdviceMethod(this.joinPoint,retVal,null);
    }
}
