package com.yc.framework.beans.config;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * //用来存储配置文件中的信息
 * //相当于保存在内存中的配置
 * @author Yanchen
 * @ClassName YCBeanDefinition
 * @Date 2019/4/11 10:08
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class YCBeanDefinition {
    private String beanClassName;
    private boolean lazyInit = false;
    private String factoryBeanName;
}
