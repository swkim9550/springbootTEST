package com.seongwoo.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
public class SampleController {

    @Autowired
    private SampleService sampleService;

    @PostMapping("/hello")
    public String hello(){
        log.info("======================");



        return "hello " + sampleService.getName();
    }
}
