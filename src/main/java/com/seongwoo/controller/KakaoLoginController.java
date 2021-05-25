package com.seongwoo.controller;


import com.seongwoo.dao.KakaoTokenResponse;
import com.seongwoo.kakao.KakaoAPI;
import com.seongwoo.kakao.KakaoConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Slf4j
@RestController
public class KakaoLoginController {

    @Autowired
    KakaoConst kakaoConst;

    private final RestTemplate restTemplate;

    public KakaoLoginController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    KakaoAPI kakaoAPI;

    @RequestMapping(value="/hello",produces = "application/json" ,method = {RequestMethod.GET,RequestMethod.POST})
    public void hello(HttpServletRequest req, @RequestParam("code") String code){

        try{
            String tokenURL = kakaoConst.getTOKEN_API_URI();
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.set("grant_type","authorization_code");
            params.set("client_id",kakaoConst.getCLIENT_ID());
            params.set("redirect_uri",kakaoConst.getREDIRECT_URI());
            params.set("code",code);
            params.set("client_secret",kakaoConst.getCLIENT_SECRET());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity formEntity = new HttpEntity<>(params, headers);
            ResponseEntity<KakaoTokenResponse> responseEntity = restTemplate.postForEntity(tokenURL , formEntity, KakaoTokenResponse.class);

            log.info("kakao token is {}.",code);
            kakaoConst.setACCESS_TOKEN(responseEntity.getBody().getAccess_token());
            kakaoAPI.callDefaultTemplateAPI(kakaoConst.getACCESS_TOKEN());

        }catch (Exception e){
            log.error(e.toString());
        }
    }
}
