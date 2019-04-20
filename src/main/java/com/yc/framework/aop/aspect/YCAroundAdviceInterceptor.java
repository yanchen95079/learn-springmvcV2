package com.yc.framework.aop.aspect;

import com.yc.framework.aop.intercept.YCMethodInterceptor;
import com.yc.framework.aop.intercept.YCMethodInvocation;

import java.lang.reflect.Method;

/**
 * @author Yanchen
 * date 2019/4/20 17:44
 */
public class YCAroundAdviceInterceptor extends YCAbstractAspectAdvice implements YCAdvice,YCMethodInterceptor {


    private YCJoinPoint joinPoint;

    public YCAroundAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(YCMethodInvocation mi) throws Throwable {

        this.joinPoint = mi;
        this.around(mi.getMethod(), mi.getArguments(), mi.getThis());
        Object retVal = mi.proceed();
        this.around(mi.getMethod(), mi.getArguments(), mi.getThis());
        return retVal;
    }

    private void around(Method method, Object[] args, Object target) throws Throwable {
         super.invokeAdviceMethod(this.joinPoint, null, null);
    }
}
