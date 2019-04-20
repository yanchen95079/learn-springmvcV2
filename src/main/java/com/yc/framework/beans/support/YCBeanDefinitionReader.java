package com.yc.framework.beans.support;

import com.yc.framework.beans.config.YCBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Yanchen
 * @ClassName YCBeanDefinitionReader
 * @Date 2019/4/11 10:32
 */
public class YCBeanDefinitionReader {
    private Properties config=new Properties();
    //固定配置文件中的key，相对于xml的规范
    private final String SCAN_PACKAGE = "scanPackage";
    private List<String> registyBeanClasses=new ArrayList<String>();

    public YCBeanDefinitionReader(String... locations){
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:",""));
        try {
            config.load(is);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        doScanner(config.getProperty(SCAN_PACKAGE));
    }

    private void doScanner(String scanPackage) {
        //转换为文件路径，实际上就是把.替换为/就OK了
        //this.getClass()
//        this.getClass().getClassLoader()
        URL url = this.getClass().getResource("/" + scanPackage.replaceAll("\\.","/"));
        File classPath = new File(url.getFile());
        for (File file : classPath.listFiles()) {
            if(file.isDirectory()){
                doScanner(scanPackage + "." + file.getName());
            }else{
                if(!file.getName().endsWith(".class")){ continue;}
                String className = (scanPackage + "." + file.getName().replace(".class",""));
                registyBeanClasses.add(className);
            }
        }
    }
    public Properties getConfig(){
        return this.config;
    }
    public List<YCBeanDefinition> loadBeanDefinitions(){
        List<YCBeanDefinition> list=new ArrayList<YCBeanDefinition>();
        try {
            for (String registyBeanClass : registyBeanClasses) {
                Class<?> beanClass = Class.forName(registyBeanClass);
                if(beanClass.isInterface()) { continue; }
                list.add(doCreateBeanDefinition
                        (toLowerFirstCase(beanClass.getSimpleName()),beanClass.getName()));
                Class<?> [] interfaces = beanClass.getInterfaces();
                for (Class<?> i : interfaces) {
                    //如果是多个实现类，只能覆盖
                    //为什么？因为Spring没那么智能，就是这么傻
                    //这个时候，可以自定义名字
                    list.add(doCreateBeanDefinition
                            (i.getName(),beanClass.getName()));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    //把每一个配信息解析成一个BeanDefinition
    private YCBeanDefinition doCreateBeanDefinition(String factoryBeanName,String beanClassName){
        YCBeanDefinition beanDefinition = new YCBeanDefinition();
        beanDefinition.setBeanClassName(beanClassName);
        beanDefinition.setFactoryBeanName(factoryBeanName);
        return beanDefinition;
    }
    private String toLowerFirstCase(String simpleName) {
        char [] chars = simpleName.toCharArray();
        //之所以加，是因为大小写字母的ASCII码相差32，
        // 而且大写字母的ASCII码要小于小写字母的ASCII码
        //在Java中，对char做算学运算，实际上就是对ASCII码做算学运算
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
