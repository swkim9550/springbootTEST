package com.seongwoo.kakao;

import com.seongwoo.dao.KakaoRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;

@Slf4j
@Service
public class KakaoAPI {

    private final RestTemplate restTemplate;

    public KakaoAPI(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Object callAPI(){

        try{
            String apiURL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiURL);

            String token = getToken();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization","Bearer 928b30b6daa4adbe5f292b7d94e641e2");
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
            Object asad = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, httpEntity, KakaoRequest.class).getBody();
            KakaoRequest responseEntity = restTemplate.exchange(builder.toUriString(), HttpMethod.POST, httpEntity, KakaoRequest.class).getBody();
        }catch (Exception e){
            log.error("error is {}",e.toString());
        }

        return "";
    }

    private String getToken() {
        try{
            String apiURL = "https://kauth.kakao.com/oauth/authorize?client_id=928b30b6daa4adbe5f292b7d94e641e2&redirect_uri=http://localhost:8080/hello&response_type=code";
            String tokenURL = "https://kauth.kakao.com/oauth/token";
            //ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiURL , String.class, 25);
            //ResponseEntity<KakaoResponse> responseEntity1351 = restTemplate.getForEntity(apiURL , KakaoResponse.class);

            KakaoRequest request = new KakaoRequest();
            request.setGrant_type("authorization_code");
            request.setClient_id("928b30b6daa4adbe5f292b7d94e641e2");
            request.setRedirect_uri("http://localhost:8080/hello");
            request.setCode("oJat_xMCzUGTVuZD32QMsRgQCjn-uzZpqFiasHwdK-ReT4ye1UK5KAFZ_En6wZ7hnmXs9wopcFAAAAF5iteRKw");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.set("grant_type","authorization_code");
            params.set("client_id","928b30b6daa4adbe5f292b7d94e641e2");
            params.set("redirect_uri","http://localhost:8080/hello");
            params.set("code","xjjWp9vtgmG6BTDzh2QghpBGaHtXRKZjlxHmJTmwqxSCCbsltBdtSaTRfLN0VmBQYi2wEgorDR4AAAF5ir8NRw");
            params.set("client_secret","oJat_xMCzUGTVuZD32QMsRgQCjn-uzZpqFiasHwdK-ReT4ye1UK5KAFZ_En6wZ7hnmXs9wopcFAAAAF5iteRKw");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity formEntity = new HttpEntity<>(params, headers);

            ResponseEntity<String> responseEntity2 = restTemplate.postForEntity(tokenURL + "", formEntity, String.class);

//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiURL);
//        HttpHeaders httpHeaders = new HttpHeaders();
//        HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
//        Object asad = restTemplate.exchange(apiURL,httpEntity,);
        }catch (Exception e){
            log.error(e.toString());
        }

        return "";
    }
}
