package com.dailydatahub.dailydatacrawler.crawl.instagram.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dailydatahub.dailydatacrawler.module.FileComponent;
import com.dailydatahub.dailydatacrawler.module.JsonComponent;
import com.dailydatahub.dailydatacrawler.module.MD5Component;
import com.dailydatahub.dailydatacrawler.module.SeleniumComponent;

@Service
public class InstagramServiceImpl implements InstagramService{

    @Autowired
    public SeleniumComponent seleniumComponent;

    @Autowired
    public JsonComponent jsonComponent;

    @Autowired
    public FileComponent fileComponent;

    @Autowired
    public MD5Component md5;

    @Value("${external.instagram.tag.url}")
    private String tagUrl;

    @Value("${external.instagram.tag.uri}")
    private String tagUri;

    private String INSTAGRAM = "인스타그램";
    private long DRIVER_TIME_OUT=10000l;
    private String InstagramId = "oshhyosung";
    private String InstagramPw = "oshh1107";
    private String instagramLoginUrl = "https://www.instagram.com/accounts/login/";

    /**
     * 태그 단위로 검색합니다.
     */
    @Override
    public JSONArray tags(String tags) throws Exception {
        return requestInterface("tags", tags);
    }

    /**
     * 검색 타입에 키워드를 같이 검색하여 검색 결과를 반환합니다.
     * @param requestType
     * @param keyword
     * @return JSONObject
     * @throws Exception
     */
    private JSONArray requestInterface(String requestType, String keyword) throws Exception{
        JSONArray array = new JSONArray();
        switch(requestType){
            case "tags":array = requestTagSearch(keyword);
                break;
            default:
                break;
        }
        return array;
    }

    /**
     * (tag) 키워드 정보로 검색하여 반환합니다.
     * @param jsonObject
     * @return JSONObject
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private JSONArray requestTagSearch(String keyword) throws Exception{
        List<String> requestUrlList = new ArrayList<>();
        driverRequestAndWait(tagUrl+tagUri+keyword,DRIVER_TIME_OUT);
        for(WebElement we : driverFindElement("article").findElements(By.tagName("a"))){
            requestUrlList.add(we.getAttribute("href"));
        }
        log(requestUrlList);
        JSONArray array = new JSONArray();
        for(int i = 0; i < requestUrlList.size(); i++) {
            try{
                array.add(requestTagSearchDetail(requestUrlList.get(i), keyword));
            }catch(Exception e){
                log("exception : " + requestUrlList.get(i));
                continue;
            }
        }
        fileComponent.exportJson(array, "D:/Workspace/Private/Dev", INSTAGRAM + "_" + keyword);
        return array;
    }

    /**
     * (tag)url 정보로 접속하여 상세 정보를 가져옵니다.
     * @param url
     * @return JSONObject
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private JSONObject requestTagSearchDetail(String url, String keyword) throws Exception{

        // 드라이버를 호출해 필요한 내용을 찾습니다.
        driverRequestAndWait(url,DRIVER_TIME_OUT);
        WebElement mainContent = driverFindElement("article");

        String regDate  =   null;
        String content  =   null;
        String title    =   null;
        String author   =   null;

        // 작성 일시
        try{
            regDate      = mainContent
                            .findElement(By.tagName("time"))
                            .getAttribute("datetime");
        }catch(Exception e){
            log("regData not Found");
        }
        
        try{
            // 작성 내용(추가 태그 포함)
            content      = mainContent
                            .findElement(By.tagName("h1"))
                            .getText()
                            .replaceAll("\n", " ");
            // 작성 내용 중 첫번째 줄
            title        = content.substring(0,30);
        }catch(Exception e){
            log("content not Found");
        }

        // 작성자 ID
        try{
            author      = mainContent
                            .findElement(By.tagName("header"))
                            .findElement(By.tagName("a"))
                            .getAttribute("href")
                            .replaceAll("/","")
                            .replaceAll("https:www.instagram.com", "");
        }catch(Exception e){
            log("author not Found");
        }

        String category     = keyword;
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("_id",      md5.md5AndHex(url));
        hashMap.put("snsId",    md5.md5AndHex(url+regDate));
        hashMap.put("url",      url);
        hashMap.put("category", category);
        hashMap.put("press",    INSTAGRAM);
        hashMap.put("regDate",  regDate);
        hashMap.put("author",   author);
        hashMap.put("content",  content);
        hashMap.put("title",    title);
        hashMap.put("status",   true);
        log(hashMap);
        return hashMapToJsonObject(hashMap);
    }

    /**
     * 드라이버를 호출합니다(크롬)
     * @return WebDriver
     * @throws Exception
     */
    private WebDriver driverCall() throws Exception{
        return seleniumComponent.getDriver();
    }

    /**
     * 현재 드라이버에서 URL 을 요청하고 지정한 시간만큼 기다립니다(1 : 1 초)
     * @param url
     * @param waitPeriod
     * @throws Exception
     */
    private void driverRequestAndWait(String url, Long waitPeriod) throws Exception{
        driverCall().get(url);
        Thread.sleep(waitPeriod);
        // URL 이 만약 로그인 화면인 경우 로그인 후 동일시간만큼 추가 대기
        if(driverCall().getCurrentUrl().contains(instagramLoginUrl)){
            WebElement webElement = driverCall().findElement(By.id("loginForm"));
            webElement.findElement(By.name("username")).sendKeys(InstagramId);
            webElement.findElement(By.name("password")).sendKeys(InstagramPw);
            webElement.submit();
            Thread.sleep(waitPeriod);
        }
    }

    /**
     * map 자료구조를 JSONObject 자료구조로 변환하여 반환합니다.
     * @param hashMap
     * @return JSONObject
     * @throws Exception
     */
    private JSONObject hashMapToJsonObject(HashMap hashMap) throws Exception{
        return new JSONObject(hashMap);
    }

    /**
     * 현재 드라이버에서 HTML 태그 엘리먼트를 검색합니다.
     * @param elementString
     * @return WebElement
     * @throws Exception
     */
    private WebElement driverFindElement(String elementString) throws Exception{
        return driverCall().findElement(By.tagName("article"));
    }

    private void log(Object obj) throws Exception{
        System.out.println(obj);
    }
    
}
