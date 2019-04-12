package com.yc.framework.context;

import com.yc.framework.beans.config.YCBeanDefinition;
import com.yc.framework.beans.support.YCBeanDefinitionReader;
import com.yc.framework.beans.support.YCDefaultListableBeanFactory;
import com.yc.framework.core.YCBeanFactory;

import java.util.List;
import java.util.Map;

/**
 * @author Yanchen
 * @ClassName YCApplicationContext
 * @Date 2019/4/11 10:23
 */
public class YCApplicationContext extends YCDefaultListableBeanFactory implements YCBeanFactory {

    private String [] configLoactions;
    private YCBeanDefinitionReader reader;

    public YCApplicationContext(String... configLoactions){
        this.configLoactions = configLoactions;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object getBean(String beanName) {
        return null;
    }

    @Override
    public void refresh() throws Exception {
       //定位配置文件
        reader=new YCBeanDefinitionReader(this.configLoactions);
        //加载配置文件 扫描相关类封装成beanDefinition
        List<YCBeanDefinition> ycBeanDefinitions = reader.loadBeanDefinitions();
        //注册 把配置放到容器中
        doRegisterBeanDefinition(ycBeanDefinitions);
        //非延迟加载的提前初始化
        doAutowrited();
    }

    private void doAutowrited() {
        for (Map.Entry<String, YCBeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            if(!beanDefinitionEntry.getValue().isLazyInit()) {
                getBean(beanName);
            }
        }
    }

    private void doRegisterBeanDefinition(List<YCBeanDefinition> beanDefinitions) throws Exception {

        for (YCBeanDefinition beanDefinition: beanDefinitions) {
            if(super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())){
                throw new Exception("The “" + beanDefinition.getFactoryBeanName() + "” is exists!!");
            }
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(),beanDefinition);
        }
        //到这里为止，容器初始化完毕
    }
}
