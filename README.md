#어노테이션 정리
@ComponentScan 하위 패키지 내에 아래에 어노테이션들을 스캔하여 빈등록
- @Configuration @Repository @Service @Controller @RestController


@EnableAutoConfiguration(@SpringBootApplication 안에 숨어 있음)
빈은 사실 두 단계로 나눠서 읽힘
- @ComponentScan
- @EnableAutoConfiguration


@EnableAutoConfiguration
- 메타파일을 읽어 온다.
- @Configuration 이 선언 된 클래스
- 기타 메타파일.. 


#외부파일에서 참조 빈을 참조해서 사용할때 샘플

@ConditionalOnMissingBean
- A프로젝트에서 B프로젝트의 빈 참조 시 A프로젝트 새롭게 정의 할 수 있도록 해주는 어노테이션
- 외부프로젝트에서 참조방법은 아래와 같이 디펜던시 등록 후 사용.
  <dependency>
      <groupId>com.recom.lib</groupId>
      <artifactId>recom-lib</artifactId>
      <version>1.0.1</version>
  </dependency>
- 외부프로젝트에서 새롭게 빈의 특성을 재정의 할 수 있다
1.    @Bean
    public RedisConfig redisConfig(){
        RedisConfig config = new RedisConfig();
        config.setName("test");
        config.setIp("localhost");
        return config;
    }
2.프로퍼티를 통하여 설정.
- 스프링 어플리케이션 실행시 웹 사용여부 프로퍼티설정
- run부분에서 바꿔줄수도 있다.
- spring.main.web-application-type=none