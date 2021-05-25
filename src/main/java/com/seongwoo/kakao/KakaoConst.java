package com.seongwoo.kakao;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class KakaoConst {

    //API URI 정보
    private String BASE_URI = "";
    private String AUTHORIZATION_CODE_API_URI = "https://kauth.kakao.com/oauth/authorize?client_id=928b30b6daa4adbe5f292b7d94e641e2&redirect_uri=http://localhost:8080/hello&response_type=code";
    private String TOKEN_API_URI = "https://kauth.kakao.com/oauth/token";
    private String REDIRECT_URI = "http://localhost:8080/hello";


    private String MESSAGE_SEND_URI = "https://kapi.kakao.com/v2/api/talk/memo/default/send"; //기본템플릿
    private String MESSAGE_SEND_URI2 = "https://kapi.kakao.com/v2/api/talk/memo/send"; //사용자 정의 템플릿

    //개인 키
    private String CLIENT_ID = "928b30b6daa4adbe5f292b7d94e641e2";
    private String CLIENT_SECRET = "d7Gw15goT7uLqUlU0EZ2993NmAbUsl8b";

    private String ACCESS_TOKEN = "";
}
