package com.example.demo;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取spring容器中所有bean的工具类，（比如接口为UserService，生成的bean为userService，默认小写）
 */
@Component   //注意要交给springboot进行管理
public class ApplicationContextUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //将容器中的bean赋值给context,为了static赋值
        this.context = applicationContext;
    }

    /**
     * 根据指定的名字获取bean对象
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        return context.getBean(name);
    }
}

