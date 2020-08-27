package com.ssky.demo;

import com.ssky.framework.annotation.PostMapping;
import com.ssky.framework.annotation.RequestParam;
import com.ssky.framework.annotation.RestController;

/**
 * Copyright (C), 2020
 *
 * @author: liuhao
 * @date: 2020/8/26 20:55
 * @description: Demo
 */

@RestController("/demo")
public class Demo {

    @PostMapping("/query")
    public void query(@RequestParam("name") String name){

        System.out.println(name);
    }

}
