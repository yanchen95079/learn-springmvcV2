package com.yc.framework.context;

/**
 * @author Yanchen
 * @ClassName YCApplicationContextAware
 * @Date 2019/4/11 10:33
 */
public interface YCApplicationContextAware {
    /**
     * Description:* 通过解耦方式获得 IOC 容器的顶层设计
     * 后面将通过一个监听器去扫描所有的类，只要实现了此接口，
     * 将自动调用 setApplicationContext()方法，从而将 IOC 容器注入到目标类中
     *      使用场景: TODO
     *      功能实现: TODO
     *      影响及风险: TODO
     * @param
     * @return
     * @throws
     * @author 闫晨(chen.yan@ucarinc.com)
     * @since 2019/4/11 10:34
     */
    void setApplicationContext(YCApplicationContext applicationContext);
}
