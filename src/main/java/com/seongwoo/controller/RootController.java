package com.seongwoo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RootController {

    Logger logger = LoggerFactory.getLogger(RootController.class);

//    @Autowired
//    RedisConfig redisConfig;

    @Autowired
    SampleService sampleService;

    @GetMapping("/hello")
    public String hello(){
        String ab = "ab입니다";
        String ac = "ac입니다";
        logger.info("test{}{}",ab,ac);
        return sampleService.getName();
    }

    @PostMapping("/testpost")
    public String testPost(@RequestBody TestDto testDto){
        return "POST테스트";
    }
}
