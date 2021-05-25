package com.seongwoo.controller;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


/**
 * RANDOM_PORT 사용시 내장 톰캣 사용으로 서블릿 환경 제공
 * 실제로 MockBean 서비스를 테스트 할 수 있다.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class KakaoLoginControllerTestRandom {

    @Autowired
    TestRestTemplate testRestTemplate;

    /**
     * Mock Service 를 이용하여 예상 값을 변경 하고 테스트
     * 기존 서비스의 getName 은 seongwoo -> whiteship 으로 변경.
     */
    @MockBean
    SampleService mockSampleService;

    @Test
    public void hello() throws Exception{

        when(mockSampleService.getName()).thenReturn("whiteship");


        String result = testRestTemplate.getForObject("/hello",String.class);
        assertThat(result).isEqualTo("hello whiteship");
    }
}
