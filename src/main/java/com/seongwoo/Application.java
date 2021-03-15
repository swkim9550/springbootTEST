package com.seongwoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

//    @Bean
//    public RedisConfig redisConfig(){
//        RedisConfig config = new RedisConfig();
//        config.setName("test");
//        config.setIp("localhost");
//        return config;
//    }



/**
 * 어노테이션 샘플
 */

//@Configuration
//@SpringBootConfiguration
//@Component // 컴포넌트 스캔을 통한 bean 등록
//@EnableAutoConfiguration //읽어온 bean 등록


/**
 * 스프링부트 구동환경 튜닝
 * @SpringBootApplication 이 아닌 컴포넌트 스캔으로 스프링 부트를 사용할때
 */
//@Configuration
//@ComponentScan
//class test{
//    public static void main2(){
//        SpringApplication springApplication = new SpringApplication(Application.class);
//        springApplication.setWebApplicationType(WebApplicationType.NONE);
//        springApplication.run("");
//    }
//}

