package com.seongwoo.scrap;

import com.seongwoo.dao.StussyItem;
import com.seongwoo.kakao.KakaoAPI;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;

@Component
@Slf4j
public class ScrapScheduler {

    @Autowired
    DataSource dataSource;

    @Autowired
    KakaoAPI kakaoAPI;


    @PostConstruct
    public void initDB(){
        try{
            log.info("Create Table & Insert");
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            String initCreateQuery = "CREATE TABLE STUSSY(ID INTEGER NOT NULL, PRODUCT_NAME VARCHAR(255), PRICE VARCHAR(255), IMAGE_URL VARCHAR(255),SOLD_OUT VARCHAR(255), PRIMARY KEY (ID) )";
            statement.executeUpdate(initCreateQuery);

            String initInsertQuery = "INSERT INTO STUSSY VALUES(1, 'test','test','test','test')";
            statement.execute(initInsertQuery);
            log.info("insert init Data finish.");
        }catch (Exception e){
            log.error(e.toString());
        }
    }


    @Scheduled(fixedDelay = 180000)
    //@Scheduled(cron = "30 * * * * *")
    //@Async
    public void cronStussy(){
        try{
            kakaoAPI.callAPI();

//            String url = "https://www.stussy.co.kr/collections/new-arrivals";
//            StussyItem stussyItem = getDocument(url);
//
//            if(stussyItem.isNewAlive()){
//                sendMessage();
//            }
        }
        catch (Exception e){
            log.error(e.toString());
        }
    }

    private void sendMessage() {
    }

    private StussyItem getDocument(String url) {
        Boolean newAlive = false;
        int compareCount = 0;
        StussyItem stussyItem = new StussyItem();

        try{
            Element body = Jsoup.connect(url).get().body();
            Element mainContent = body.getElementById("MainContent");
            Elements itemList = mainContent.select("li.collection__product");

            for(Element item : itemList){
                compareCount++;
                String soldOutString = "재고있음";
                String price = item.getElementsByClass("product-card__price").get(0).text();
                Elements soldOutElement = item.getElementsByClass("product-card__variant-overlay");

                if(soldOutElement.size() > 0 ){
                    soldOutString = soldOutElement.get(0).text();
                }

                String imgUrl = item.getElementsByClass("lazyload").attr("src");
                String productName = item.getElementsByTag("a").text();

                stussyItem.setProductName(productName);
                stussyItem.setPrice(price);
                stussyItem.setImgUrl(imgUrl);
                stussyItem.setSoldOutString(soldOutString);

                log.info("상품명: {}, 가격: {}, 이미지URL: {}, 품절여부: {}",productName,price,imgUrl,soldOutString);
                newAlive = checkDB(stussyItem);

                if(newAlive){
                    stussyItem.setNewAlive(true);
                    return stussyItem;
                }else{
                    log.info("신상품이 없습니다.");
                }

                if(compareCount == 1){
                    break;
                }
            }
        }catch (Exception e){
            log.error("error is {}", e.toString());
        }
        return stussyItem;
    }

    private Boolean checkDB(StussyItem stussyItem) {
        try{
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            String selectQuery = "SELECT * FROM " +
                                    "STUSSY";
            ResultSet rs = statement.executeQuery(selectQuery);

            while (rs.next()){
                //PRODUCT_NAME VARCHAR(255), PRICE VARCHAR(255), IMAGE_URL VARCHAR(255),SOLD_OUT VARCHAR(255)
                String PRODUCT_NAME = rs.getString("PRODUCT_NAME");
                String PRICE = rs.getString("PRICE");
                String IMAGE_URL = rs.getString("IMAGE_URL");
                String SOLD_OUT = rs.getString("SOLD_OUT");

                log.info("DB data is ..{},{},{},{}",PRODUCT_NAME,PRICE,IMAGE_URL,SOLD_OUT);
                if(!IMAGE_URL.equals(stussyItem.getImgUrl())) {
                    log.info("DB와 데이터 다름.");
                    updateDB(stussyItem);
                }
                else{
                    return false;
                }
            }
        }catch (Exception e){
            log.error(e.toString());
        }
        return true;
    }

    private void updateDB(StussyItem stussyItem) {
        try{
            //ID,PRODUCT_NAME VARCHAR(255), PRICE VARCHAR(255), IMAGE_URL VARCHAR(255),SOLD_OUT VARCHAR(255)
            Connection connection = dataSource.getConnection();
            String updateQuery = "UPDATE STUSSY SET PRODUCT_NAME=?, PRICE=? ,IMAGE_URL=?,SOLD_OUT=? WHERE ID= ?";
            PreparedStatement pstmt = connection.prepareStatement(updateQuery);
            pstmt.setString(1,stussyItem.getProductName());
            pstmt.setString(2,stussyItem.getPrice());
            pstmt.setString(3,stussyItem.getImgUrl());
            pstmt.setString(4,stussyItem.getSoldOutString());
            pstmt.setString(5,"1");

            int insertTrue = pstmt.executeUpdate();
            log.info("change list=",insertTrue);


        }catch (SQLException e){
            log.error("SQL Error {}",e.toString());
        }
    }
}
