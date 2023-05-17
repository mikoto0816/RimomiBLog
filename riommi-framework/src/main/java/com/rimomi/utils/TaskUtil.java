package com.rimomi.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 定时任务中类型注入
 */
//@Component
public class TaskUtil implements ApplicationContextAware {
    private static ApplicationContext context;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
    public static ApplicationContext getApplicationContext(){
        return context;
    }
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }
}
