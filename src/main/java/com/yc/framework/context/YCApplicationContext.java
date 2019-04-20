package com.yc.framework.context;

import com.yc.framework.annotation.YCAutowired;
import com.yc.framework.annotation.YCController;
import com.yc.framework.annotation.YCService;
import com.yc.framework.aop.YCAopProxy;
import com.yc.framework.aop.YCCglibAopProxy;
import com.yc.framework.aop.YCJdkDynamicAopProxy;
import com.yc.framework.aop.config.YCAopConfig;
import com.yc.framework.aop.support.YCAdvisedSupport;
import com.yc.framework.beans.YCBeanWrapper;
import com.yc.framework.beans.config.YCBeanDefinition;
import com.yc.framework.beans.config.YCBeanPostProcessor;
import com.yc.framework.beans.support.YCBeanDefinitionReader;
import com.yc.framework.beans.support.YCDefaultListableBeanFactory;
import com.yc.framework.core.YCBeanFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yanchen
 * @ClassName YCApplicationContext
 * @Date 2019/4/11 10:23
 */
public class YCApplicationContext extends YCDefaultListableBeanFactory implements YCBeanFactory {

    private String [] configLocations;
    private YCBeanDefinitionReader reader;
    /**
     * 单例的IOC容器缓存
    */
     private Map<String,Object> factoryBeanObjectCache = new ConcurrentHashMap<String, Object>();
    /**
    * 通用的IOC容器
    */
    private Map<String,YCBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<String, YCBeanWrapper>();
    public YCApplicationContext(String... configLocations){
        this.configLocations = configLocations;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //依赖注入，从这里开始，通过读取BeanDefinition中的信息
    //然后，通过反射机制创建一个实例并返回
    //Spring做法是，不会把最原始的对象放出去，会用一个BeanWrapper来进行一次包装
    //装饰器模式：
    //1、保留原来的OOP关系
    //2、我需要对它进行扩展，增强（为了以后AOP打基础）
    @Override
    public Object getBean(String beanName) throws Exception{
        YCBeanDefinition gpBeanDefinition = this.beanDefinitionMap.get(beanName);
        Object instance = null;

        //这个逻辑还不严谨，自己可以去参考Spring源码
        //工厂模式 + 策略模式
       YCBeanPostProcessor postProcessor = new YCBeanPostProcessor();

        postProcessor.postProcessBeforeInitialization(instance,beanName);

        instance = instantiateBean(beanName,gpBeanDefinition);

        //3、把这个对象封装到BeanWrapper中
        YCBeanWrapper beanWrapper = new YCBeanWrapper(instance);

        //4、把BeanWrapper存到IOC容器里面
//        //1、初始化

//        //class A{ B b;}
//        //class B{ A a;}
//        //先有鸡还是先有蛋的问题，一个方法是搞不定的，要分两次

        //2、拿到BeanWraoper之后，把BeanWrapper保存到IOC容器中去
        this.factoryBeanInstanceCache.put(beanName,beanWrapper);

        postProcessor.postProcessAfterInitialization(instance,beanName);

//        //3、注入
        populateBean(beanName,new YCBeanDefinition(),beanWrapper);


        return this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();
    }

    private Object instantiateBean(String beanName, YCBeanDefinition ycBeanDefinition) {
        //1、拿到要实例化的对象的类名
        String className = ycBeanDefinition.getBeanClassName();

        //2、反射实例化，得到一个对象
        Object instance = null;
        try {
//            ycBeanDefinition.getFactoryBeanName()
            //假设默认就是单例,细节暂且不考虑，先把主线拉通
            if(this.factoryBeanObjectCache.containsKey(className)){
                instance = this.factoryBeanObjectCache.get(className);
            }else {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();


                YCAdvisedSupport config = intentionAopConfig(ycBeanDefinition);
                config.setTargetClass(clazz);
                config.setTarget(instance);

                //符合PointCut的规则的话，闯将代理对象
                if(config.pointCutMatch()) {
                    instance = createProxy(config).getProxy();
                }
                this.factoryBeanObjectCache.put(className,instance);
                this.factoryBeanObjectCache.put(ycBeanDefinition.getFactoryBeanName(),instance);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return instance;
    }
    private YCAopProxy createProxy(YCAdvisedSupport config) {

        Class targetClass = config.getTargetClass();
        if(targetClass.getInterfaces().length > 0){
            return new YCJdkDynamicAopProxy(config);
        }
        return new YCCglibAopProxy(config);
    }
    private YCAdvisedSupport intentionAopConfig(YCBeanDefinition ycBeanDefinition) {
        YCAopConfig config = new YCAopConfig();
        config.setPointCut(this.reader.getConfig().getProperty("pointCut"));
        config.setAspectClass(this.reader.getConfig().getProperty("aspectClass"));
        config.setAspectBefore(this.reader.getConfig().getProperty("aspectBefore"));
        config.setAspectAfter(this.reader.getConfig().getProperty("aspectAfter"));
        config.setAspectAround(this.reader.getConfig().getProperty("aspectAround"));
        config.setAspectAfterThrow(this.reader.getConfig().getProperty("aspectAfterThrow"));
        config.setAspectAfterThrowingName(this.reader.getConfig().getProperty("aspectAfterThrowingName"));
        return new YCAdvisedSupport(config);
    }
    private void populateBean(String beanName, YCBeanDefinition ycBeanDefinition, YCBeanWrapper ycBeanWrapper) {
        Object instance = ycBeanWrapper.getWrappedInstance();

        Class<?> clazz = ycBeanWrapper.getWrappedClass();
        //判断只有加了注解的类，才执行依赖注入
        if(!(clazz.isAnnotationPresent(YCController.class) || clazz.isAnnotationPresent(YCService.class))){
            return;
        }

        //获得所有的fields
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if(!field.isAnnotationPresent(YCAutowired.class)){ continue;}

            YCAutowired autowired = field.getAnnotation(YCAutowired.class);

            String autowiredBeanName =  autowired.value().trim();
            if("".equals(autowiredBeanName)){
                autowiredBeanName = field.getType().getName();
            }

            //强制访问
            field.setAccessible(true);

            try {

                if(this.factoryBeanInstanceCache.get(autowiredBeanName) == null){ continue; }

                field.set(instance,this.factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

    }
    @Override
    public Object getBean(Class<?> beanClass) throws Exception {
        return getBean(beanClass.getName());
    }
    @Override
    public void refresh() throws Exception {
       //定位配置文件
        reader=new YCBeanDefinitionReader(this.configLocations);
        //加载配置文件 扫描相关类封装成beanDefinition
        List<YCBeanDefinition> ycBeanDefinitions = reader.loadBeanDefinitions();
        //注册 把配置放到容器中
        doRegisterBeanDefinition(ycBeanDefinitions);
        //非延迟加载的提前初始化
        doAutowired();
    }

    private void doAutowired() {
        for (Map.Entry<String, YCBeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            if(!beanDefinitionEntry.getValue().isLazyInit()) {
                try {
                    getBean(beanName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
    public Properties getConfig(){
        return this.reader.getConfig();
    }
    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new  String[this.beanDefinitionMap.size()]);
    }

    public int getBeanDefinitionCount(){
        return this.beanDefinitionMap.size();
    }

}
