package com.seongwoo.controller;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

/**
 * 통합테스트가 아닌 특정 컨트롤러만 테스트하기 위한 테스트 케이스
 * bean 하나만 등록해서 테스트
 * 슬라이스 테스트 레이어 별로 잘라서 테스트하고 싶을때
 *
 * @JsonTest
 * @WebMvcTest
 * @WebFluxTest
 * @DataJpaTest
 */

@RunWith(SpringRunner.class)
@WebMvcTest(KakaoLoginController.class)
public class SampleOneTest {

    @MockBean
    SampleService mockSampleService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void hello() throws Exception {
        when(mockSampleService.getName()).thenReturn("whiteship");

        mockMvc.perform(get("/hello"))
                .andExpect(content().string("hello whiteship"));
    }
}
