package com.yc.framework.aop;


import com.yc.framework.aop.support.YCAdvisedSupport;

/**
 * Created by Tom on 2019/4/14.
 */
public class YCCglibAopProxy implements YCAopProxy {
    public YCCglibAopProxy(YCAdvisedSupport config) {
    }

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}
