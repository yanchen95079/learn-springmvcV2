package com.yc.framework.aop.aspect;

import com.yc.framework.aop.intercept.YCMethodInterceptor;
import com.yc.framework.aop.intercept.YCMethodInvocation;

import java.lang.reflect.Method;

/**
 * Created by Tom on 2019/4/15.
 */
public class YCMethodBeforeAdviceInterceptor extends YCAbstractAspectAdvice implements YCAdvice,YCMethodInterceptor {


    private YCJoinPoint joinPoint;
    public YCMethodBeforeAdviceInterceptor(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    private void before(Method method,Object[] args,Object target) throws Throwable{
        //传送了给织入参数
        //method.invoke(target);
        super.invokeAdviceMethod(this.joinPoint,null,null);

    }
    @Override
    public Object invoke(YCMethodInvocation mi) throws Throwable {
        //从被织入的代码中才能拿到，JoinPoint
        this.joinPoint = mi;
        before(mi.getMethod(), mi.getArguments(), mi.getThis());
        return mi.proceed();
    }
}
