package com.seongwoo.dao;

import lombok.Data;

@Data
public class KakaoResponse {
    private String code;
    private String state;
    private String error;
    private String error_description;
}
