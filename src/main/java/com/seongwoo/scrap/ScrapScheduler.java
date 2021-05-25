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

    @Autowired
    QueryString queryString;

    @PostConstruct
    public void initDB(){
        try{
            log.info("Create Table & Insert");
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            String initCreateQuery = queryString.getCREATE_QUERY();
            statement.executeUpdate(initCreateQuery);

            String initInsertQuery = queryString.getINIT_INSERT_QUERY();
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
            //sendMessage();
            String url = "https://www.stussy.co.kr/collections/tees";
            StussyItem stussyItem = getDocument(url);

            if(stussyItem.isNewAlive()){
                //sendMessage();
            }
        }
        catch (Exception e){
            log.error(e.toString());
        }
    }

    private void sendMessage() {
        kakaoAPI.getToken();
        //kakaoAPI.callAPI();
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

    public Boolean selectDB(StussyItem stussyItem){
        try{
            Connection connection = getDBConnection();
            Statement statement = connection.createStatement();
            String selectQuery = queryString.getSELECT_QUERY();;
            ResultSet rs = statement.executeQuery(selectQuery);

            while (rs.next()){
                stussyItem.setProductName(rs.getString("PRODUCT_NAME"));
                stussyItem.setImgUrl("https:"+rs.getString("IMAGE_URL"));
                stussyItem.setPrice(rs.getString("PRICE"));
                stussyItem.setSoldOutString(rs.getString("SOLD_OUT"));
            }
        }catch (Exception e){
            log.error(e.toString());
        }
        return true;
    }

    public Boolean checkDB(StussyItem stussyItem) {
        try{
            Connection connection = getDBConnection();
            Statement statement = connection.createStatement();
            String selectQuery = queryString.getSELECT_QUERY();;
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

    public void updateDB(StussyItem stussyItem) {
        try{
            //ID,PRODUCT_NAME VARCHAR(255), PRICE VARCHAR(255), IMAGE_URL VARCHAR(255),SOLD_OUT VARCHAR(255)
            Connection connection = dataSource.getConnection();
            String updateQuery = queryString.getUPDATE_QUERY();
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

    public Connection getDBConnection(){
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        }catch (Exception e){
            log.error("getDBConnection() error. {}",e.toString());
        }
        return connection;
    }

}
