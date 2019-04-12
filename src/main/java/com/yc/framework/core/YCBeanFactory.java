package com.yc.framework.core;

/**
 * @author Yanchen
 * @ClassName YCBeanFactory
 * @Date 2019/4/11 10:01
 */
public interface YCBeanFactory {
    /**
     * Description: 根据beanName从IOC容器中获得一个实例Bean
     *      使用场景: TODO
     *      功能实现: TODO
     *      影响及风险: TODO
     * @param   beanName
     * @return
     * @throws
     * @author 闫晨(chen.yan@ucarinc.com)
     * @since 2019/4/11 10:02
     */
    Object getBean(String beanName);
}
