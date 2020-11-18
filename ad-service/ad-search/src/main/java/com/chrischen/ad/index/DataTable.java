package com.chrischen.ad.index;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Chris Chen
 * save all index beans to a map
 * when using a particular bean, we just load and save it into the map
 */

@Component
public class DataTable implements ApplicationContextAware, PriorityOrdered {

    private static ApplicationContext applicationContext;

    public static final Map<Class, Object> dataTableMap = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DataTable.applicationContext = applicationContext;
        System.out.println("DataTable setApplicationContext works");
    }

    //the less the value is, higher order it maintains
    @Override
    public int getOrder() {
        return PriorityOrdered.HIGHEST_PRECEDENCE;
    }

    @SuppressWarnings("all")
    public static <T> T of(Class<T> clazz) {
        T instance = (T) dataTableMap.get(clazz);
        if (null != instance) {
            return instance;
        }

        dataTableMap.put(clazz, bean(clazz));
        return (T) dataTableMap.get(clazz);
    }

    //get bean by name
    @SuppressWarnings("all")
    private static <T> T bean(String beanName) {
        return (T) applicationContext.getBean((beanName));
    }

    //get bean by class
//    @SuppressWarnings("all")
    private static <T> T bean(Class clazz) {
//        System.out.println("-----------------------");
        System.out.println(clazz);
        System.out.println(applicationContext);
//        System.out.println(null == (T) applicationContext.getBean((clazz)));
        return (T) applicationContext.getBean((clazz));
    }
}
