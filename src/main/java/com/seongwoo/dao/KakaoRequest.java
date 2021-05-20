package com.seongwoo.dao;

import lombok.Data;

@Data
public class KakaoRequest {
    private String grant_type;
    private String client_id;
    private String redirect_uri;
    private String code;
    private String client_secret;

}
