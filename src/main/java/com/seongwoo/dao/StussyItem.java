package com.seongwoo.dao;

import lombok.Data;

@Data
public class StussyItem {
    private String productName;
    private String price;
    private String imgUrl;
    private String soldOutString;
    private boolean newAlive;
}
