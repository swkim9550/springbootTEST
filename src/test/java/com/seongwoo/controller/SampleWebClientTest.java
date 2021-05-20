package com.seongwoo.controller;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

/**
 * 스프링 웹플럭스에서 제공해주는 webClient 를 활용한 테스트케이스
 * 좀 더 직관적이라는 장점?
 * 리액티브는 이벤트드리븐 방식의 비동기 Rest Client 중 하나이다.
 * 요청을 보내고 기다리는 것이 아닌 응답이 오면 코드 실행.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SampleWebClientTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    SampleService mockSampleService;

    @Test
    public void hello() throws Exception{


        Map<String,String> test = new HashMap<>();

        when(mockSampleService.getName()).thenReturn("whiteship");
        webTestClient.get().uri("/hello")
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .expectBody(String.class)
                            .isEqualTo("hello whiteship");

    }
}
