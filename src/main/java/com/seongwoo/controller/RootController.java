package com.seongwoo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

//    @Autowired
//    RedisConfig redisConfig;

    @Autowired
    SampleService sampleService;

    @GetMapping("/hello")
    public String hello(){
        return sampleService.getName();
    }
}
