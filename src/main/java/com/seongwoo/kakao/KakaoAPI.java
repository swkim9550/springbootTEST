package com.seongwoo.kakao;

import com.seongwoo.dao.StussyItem;
import com.seongwoo.scrap.ScrapScheduler;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;


@Slf4j
@Service
public class KakaoAPI {
    @Autowired
    KakaoConst kakaoConst;

    @Autowired
    ScrapScheduler scrapScheduler;

    private final RestTemplate restTemplate;

    public KakaoAPI(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String callDefaultTemplateAPI(String ACCESS_TOKEN){

        try{
            String apiURL = kakaoConst.getMESSAGE_SEND_URI();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Authorization","Bearer "+ACCESS_TOKEN);
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, JSONObject> param = new LinkedMultiValueMap<>();
            JSONObject template_object = new JSONObject();
            template_object.put("object_type","feed");

            JSONObject contentObject = new JSONObject();
            StussyItem stussyItem = new StussyItem();

            boolean dbCheck = scrapScheduler.selectDB(stussyItem);

            contentObject.put("title","신상품 "+stussyItem.getProductName());
            contentObject.put("description",stussyItem.getPrice());
            contentObject.put("image_url",stussyItem.getImgUrl());
            contentObject.put("image_width","240");
            contentObject.put("image_height","240");

            JSONObject linkObject = new JSONObject();
            linkObject.put("web_url","https://www.stussy.co.kr/collections/tees");
            linkObject.put("mobile_web_url","https://developers.kakao.com");
            contentObject.put("link",linkObject);
            template_object.put("content",contentObject);

            JSONArray buttonArray = new JSONArray();
            JSONObject buttonObject = new JSONObject();
            buttonObject.put("title","웹으로 이동");

            JSONObject buttonLinkObject = new JSONObject();
            buttonLinkObject.put("web_url","https://www.stussy.co.kr/collections/tees");
            buttonObject.put("link",buttonLinkObject);

            buttonArray.put(buttonObject);
            template_object.put("buttons",buttonArray);
            param.add("template_object",template_object);

            HttpEntity<?> httpEntity = new HttpEntity<>(param,httpHeaders);
            restTemplate.postForEntity(apiURL, httpEntity, Object.class);

        }catch (Exception e){
            log.error("error is {}",e.toString());
        }
        return "";
    }

    public String callTemplateAPI(String ACCESS_TOKEN){
        return null;
    }

    public String getFinalURL(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setInstanceFollowRedirects(false);
        con.connect();
        con.getInputStream();

        if (con.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM || con.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
            String redirectUrl = con.getHeaderField("Location");
            return getFinalURL(redirectUrl);
        }
        return url;
    }

    public void getToken() {
        try{
            String apiURL = kakaoConst.getAUTHORIZATION_CODE_API_URI();
            URI uri = new URI(apiURL);
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri , String.class);
        }catch (Exception e){
            log.error(e.toString());
        }
    }
}
