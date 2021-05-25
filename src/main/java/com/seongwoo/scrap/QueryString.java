package com.seongwoo.scrap;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class QueryString {
    private String CREATE_QUERY = "CREATE TABLE STUSSY(ID INTEGER NOT NULL, PRODUCT_NAME VARCHAR(255), PRICE VARCHAR(255), IMAGE_URL VARCHAR(255),SOLD_OUT VARCHAR(255), PRIMARY KEY (ID) )";
    private String INIT_INSERT_QUERY = "INSERT INTO STUSSY VALUES(1, 'test','test','test','test')";
    private String SELECT_QUERY = "SELECT * FROM " + "STUSSY";
    private String UPDATE_QUERY = "UPDATE STUSSY SET PRODUCT_NAME=?, PRICE=? ,IMAGE_URL=?,SOLD_OUT=? WHERE ID= ?";
}
