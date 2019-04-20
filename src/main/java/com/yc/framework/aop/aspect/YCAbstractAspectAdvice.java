package com.yc.framework.aop.aspect;

import java.lang.reflect.Method;

/**
 * Created by Tom on 2019/4/15.
 */
public abstract class YCAbstractAspectAdvice implements YCAdvice {
    private Method aspectMethod;
    private Object aspectTarget;
    public YCAbstractAspectAdvice(Method aspectMethod, Object aspectTarget) {
       this.aspectMethod = aspectMethod;
       this.aspectTarget = aspectTarget;
    }

    public Object invokeAdviceMethod(YCJoinPoint joinPoint, Object returnValue, Throwable tx) throws Throwable{
        Class<?> [] paramTypes = this.aspectMethod.getParameterTypes();
        if(null == paramTypes || paramTypes.length == 0){
            return this.aspectMethod.invoke(aspectTarget);
        }else{
            Object [] args = new Object[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i ++) {
                if(paramTypes[i] == YCJoinPoint.class){
                    args[i] = joinPoint;
                }else if(paramTypes[i] == Throwable.class){
                    args[i] = tx;
                }else if(paramTypes[i] == Object.class){
                    args[i] = returnValue;
                }
            }
            return this.aspectMethod.invoke(aspectTarget,args);
        }
    }


}
