package com.rimomi.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * bean拷贝工具类
 */
public class BeanCopyUtils {

    public BeanCopyUtils() {
    }

    public static <V> V copyBean(Object source, Class<V> target) {
        V result = null;
        try {
            //创建目标对象
            result = target.newInstance();
            //实现bean拷贝
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        返回结果
        return result;
    }

    /**
     * bean拷贝list类型封装
     *
     */
    public static <O,V> List<V> copyBeanList(List<O> list , Class<V> target){
        //对list进行bean拷贝
        List<V> collect = list.stream()
                .map(o -> copyBean(o, target))
                .collect(Collectors.toList());
        //返回collect
        return collect;
    }
}
