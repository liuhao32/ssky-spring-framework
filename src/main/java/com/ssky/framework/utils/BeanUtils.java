package com.ssky.framework.utils;

/**
 * Copyright (C), 2020
 *
 * @author: liuhao
 * @date: 2020/8/26 21:25
 * @description: Bean工具类
 */

public class BeanUtils {

    /**
     * 首字母小写
     * @param beanName beanName
     * @return 首字母小写beanName
     */
    public static String lowerFirstCase(String beanName){
        char[] chars = beanName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
