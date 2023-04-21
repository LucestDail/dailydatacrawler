package com.dailydatahub.dailydatacrawler.crawl.instagram.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

    @Value("${external.instagram.explore.uri}")
    private String exploreUri;

    @Value("${crawler.json.save.path}")
    private String crawlerJsonSavePath;

    private String INSTAGRAM = "INSTAGRAM";
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
     * get all of data from instagram explore tab
     */
    @Override
    public JSONArray explore() throws Exception {
        return requestInterface("explore", null);
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
        accessLoginPage();
        switch(requestType){
            case "tags":array = requestTagSearch(keyword);
                break;
            case "explore":array = requestExplore();
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
    private JSONArray requestExplore() throws Exception{
        log("<PROCESS> access to content list page >>> " + tagUrl + exploreUri);
        List<String> requestUrlList = new ArrayList<>();
        try{
            driverRequestAndWait(tagUrl+exploreUri);
            scrollDownRequestAndWait();
        }catch(Exception e){
            log("<EXCEPTION> can not request and wait process");
            return null;
        }
        for(WebElement we : driverFindElement("main").findElements(By.tagName("a"))){
            requestUrlList.add(we.getAttribute("href"));
        }
        JSONArray array = new JSONArray();
        for(int i = 0; i < requestUrlList.size(); i++) {
            try{
                array.add(requestTagSearchDetail(requestUrlList.get(i), "request"));
            }catch(Exception e){
                log("exception : " + requestUrlList.get(i));
                continue;
            }
        }
        fileComponent.exportJson(array, crawlerJsonSavePath, INSTAGRAM + "_" + "explore");
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
        try{
            driverRequestAndWait(tagUrl+tagUri+keyword);
            scrollDownRequestAndWait();
        }catch(Exception e){
            log("<EXCEPTION> can not request and wait process");
            return null;
        }
        for(WebElement we : driverFindElement("article").findElements(By.tagName("a"))){
            requestUrlList.add(we.getAttribute("href"));
        }
        JSONArray array = new JSONArray();
        for(int i = 0; i < requestUrlList.size(); i++) {
            try{
                array.add(requestTagSearchDetail(requestUrlList.get(i), keyword));
            }catch(Exception e){
                log("exception : " + requestUrlList.get(i));
                continue;
            }
        }
        fileComponent.exportJson(array, crawlerJsonSavePath, INSTAGRAM + "_" + keyword);
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
        driverRequestAndWait(url);
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
        log("<PROCESS> JSONObject Allocated : " + url);
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
    private void driverRequestAndWait(String url) throws Exception{
        boolean success = false;
        int retries = 0;
        int maxTryCount = 10;
        while (!success && retries < maxTryCount) {
            try {
                log("<PROCESS> access to content page");
                driverCall().get(url);
                if(retries > 1){
                    driverCall().navigate().refresh();
                }
                Thread.sleep(DRIVER_TIME_OUT);
                log("<PROCESS> access to content page loaded");
                success = true;
            } catch (Exception e) {
                log("<EXCEPTION> content page loaded failed and request count : " + retries++);
                continue;
            }
        }
    }

    private void scrollDownRequestAndWait() throws Exception{
        log("<PROCESS> content page list scroll down");
        long currentDate = new Date().getTime();
        while (new Date().getTime() < currentDate + DRIVER_TIME_OUT) { //scroll down 10 sec
            Thread.sleep(1000); //thread hold and wait
            //executeScript: JavaScript source execute
            ((JavascriptExecutor)driverCall()).executeScript("window.scrollTo(0, document.body.scrollHeight)", driverCall().findElement(By.tagName("main")));
            log("<PROCESS> content page scroll down");
        }
    }

    /**
     * access home page and do login process
     * @param url
     * @param waitPeriod
     * @throws Exception
     */
    private void accessLoginPage() throws Exception{
        try {
            log("<PROCESS> try login page");
            driverCall().get(instagramLoginUrl);
            Thread.sleep(DRIVER_TIME_OUT);
            log("<PROCESS> try login page loaded");
            if(driverCall().findElements(By.id("loginForm")).size() != 0){
                log("<PROCESS> try login page detected");
                WebElement webElement = driverCall().findElement(By.id("loginForm"));
                webElement.findElement(By.name("username")).sendKeys(InstagramId);
                webElement.findElement(By.name("password")).sendKeys(InstagramPw);
                webElement.submit();
                Thread.sleep(DRIVER_TIME_OUT);
            }
        } catch (Exception e) {
            System.out.println("exception : Timed out receiving message from renderer >> " + instagramLoginUrl);
            driverCall().navigate().refresh();
            log("<PROCESS> try login page");
            driverCall().get(instagramLoginUrl);
            Thread.sleep(DRIVER_TIME_OUT);
            log("<PROCESS> try login page loaded");
            if(driverCall().findElements(By.id("loginForm")).size() != 0){
                log("<PROCESS> try login page detected");
                WebElement webElement = driverCall().findElement(By.id("loginForm"));
                webElement.findElement(By.name("username")).sendKeys(InstagramId);
                webElement.findElement(By.name("password")).sendKeys(InstagramPw);
                webElement.submit();
                Thread.sleep(DRIVER_TIME_OUT);
            }
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
        return driverCall().findElement(By.tagName(elementString));
    }

    private void log(Object obj) throws Exception{
        System.out.println(obj);
    }
    
}
