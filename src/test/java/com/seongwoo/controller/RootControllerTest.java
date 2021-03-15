package com.seongwoo.controller;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.OutputCaptureRule;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * TEST 방법
 * 1.MockMvc 를 사용 하여 테스트 가능하나 이전버전 까지만 되는 것 같음 스프링 버전에 따라서 안되는 경우 발생...그리고 자동완성이 잘안됨
 * 2.TestRestTemplate 를 활용하여 랜덤포트로 테스트 케이스 임베디드를 올려서 사용.
 * 3.WebTestClient 를 활용한 테스트 (async) -restClient API중 하나이다.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RootControllerTest {

    //log,sysout 등을 찾아준다
    @Rule
    public OutputCaptureRule outputCaptureRule = new OutputCaptureRule();

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    WebTestClient webTestClient;

    //서비스를 mock 샘플 서비스를 사용한다.
    @MockBean
    SampleService sampleService;

    /**
     * TestRestTemplate사용
     * @throws Exception
     */
    @Test
    public void hello() throws Exception{
        when(sampleService.getName()).thenReturn("test");
        String result = testRestTemplate.getForObject("/hello",String.class);
        assertThat(result).isEqualTo("test");
        assertThat(outputCaptureRule.toString()).contains("ab입니다").contains("ac입니다");
    }

    /**
     * mockMvc
     * @throws Exception
     */
    //import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
    //import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
    //import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
    //import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
    @Test
    public void hello2() throws Exception{
        when(sampleService.getName()).thenReturn("test");
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("test"))
                .andDo(print());
    }

    /**
     * WebTestClient 사용 pom.xml 에 webFlux 추가 필요.
     * @throws Exception
     */
    @Test
    public void hello3() throws Exception{
        when(sampleService.getName()).thenReturn("test");
        webTestClient.get().uri("/hello").exchange()
                                            .expectStatus().isOk()
                                            .expectBody(String.class).isEqualTo("test");
    }
    /**
     * webTestClient 심화
     */
    @Test
    public void hello4() throws Exception{
        String inputJson = "{\"name\":\"test\",\"content\":\"test\"}";
        webTestClient.method(HttpMethod.GET)
                .uri("/hello")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(inputJson),String.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    /**
     * webTestClient 심화
     */
    @Test
    public void hello5() throws Exception{
        TestDto testDto = new TestDto();
        testDto.setId("kbkkim");
        testDto.setName("김성우");
        testDto.setPhone("0109999999");

        webTestClient.method(HttpMethod.POST)
                .uri("/testpost")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(testDto))
                .exchange()
                .expectStatus()
                .isOk();

        //Mockito.verify(repository, times(1)).save(employee);
    }
}


