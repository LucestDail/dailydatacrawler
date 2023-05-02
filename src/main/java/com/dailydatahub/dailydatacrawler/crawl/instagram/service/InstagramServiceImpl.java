package com.dailydatahub.dailydatacrawler.crawl.instagram.service;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dailydatahub.dailydatacrawler.crawl.instagram.dao.domain.Instagram;
import com.dailydatahub.dailydatacrawler.crawl.instagram.dao.repositorty.InstagramRepository;
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
    private InstagramRepository instagramRepository;

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

    @Value("${crawler.access.count.max}")
    private int maxTryCount;

    private String INSTAGRAM = "INSTAGRAM";
    private long DRIVER_TIME_OUT=10000l;
    private String InstagramId = "oshhyosung";
    private String InstagramPw = "oshh1107";
    private String instagramLoginUrl = "https://www.instagram.com/accounts/login/";
    private int CONTENTS_SCRAP_MAX = 100;
    private int CONTENTS_SCRAP_TRY_MAX = 10;
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
     * get all of data from instagram explore tab
     */
    @Override
    public JSONArray exploreSave() throws Exception {
        return requestInterface("exploreSave", null);
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
            case "explore":array = requestExploreSave();
                break;
            case "exploreSave":
                requestExploreSave();
                break;
            default:
                break;
        }
        return array;
    }

    /**
     * (tag) 무작위 정보를 검색하여 반환합니다.
     * @param jsonObject
     * @return JSONObject
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private JSONArray requestExplore() throws Exception{
        log("<PROCESS> access to content list page >>> " + tagUrl + exploreUri);

        Set<String> requestUrlSet = new LinkedHashSet<String>();
        
        try{
            driverRequestAndWait(tagUrl+exploreUri);
            requestUrlSet = scrollDownRequestAndWaitAndGetUrl(requestUrlSet);
            log("<PROCESS> current request URL Set Scraped Size : "+ requestUrlSet.size() + " >>> request start ");
        }catch(Exception e){
            e.printStackTrace();
            log("<EXCEPTION> can not request and wait process");
            return null;
        }

        JSONArray array = new JSONArray();
        JSONArray arrayComment = new JSONArray();

        for(String requestUrl : requestUrlSet){
            try{
                driverRequestAndWait(requestUrl);
                array.add(requestTagSearchDetail(requestUrl, "explore"));
                arrayComment = scrapCommentList(arrayComment, requestUrl, "explore");
            }catch(Exception e){
                log("exception : " + requestUrl);
                continue;
            }
        }

        fileComponent.exportJson(array, crawlerJsonSavePath, INSTAGRAM + "_" + "explore");
        fileComponent.exportJson(arrayComment, crawlerJsonSavePath, INSTAGRAM + "_COMMENT_" + "explore");
        return array;
    }

/**
     * (tag) 무작위 정보를 검색하여 반환합니다.
     * @param jsonObject
     * @return JSONObject
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private JSONArray requestExploreSave() throws Exception{
        log("<PROCESS> access to content list page >>> " + tagUrl + exploreUri);

        Set<String> requestUrlSet = new LinkedHashSet<String>();
        
        try{
            driverRequestAndWait(tagUrl+exploreUri);
            requestUrlSet = scrollDownRequestAndWaitAndGetUrl(requestUrlSet);
            log("<PROCESS> current request URL Set Scraped Size : "+ requestUrlSet.size() + " >>> request start ");
        }catch(Exception e){
            e.printStackTrace();
            log("<EXCEPTION> can not request and wait process");
            return null;
        }

        JSONArray array = new JSONArray();
        JSONArray arrayComment = new JSONArray();

        for(String requestUrl : requestUrlSet){
            try{
                driverRequestAndWait(requestUrl);
                array.add(requestTagSearchDetail(requestUrl, "explore"));
                arrayComment = scrapCommentList(arrayComment, requestUrl, "explore");
            }catch(Exception e){
                log("exception : " + requestUrl);
                continue;
            }
        }
        saveInstagram(array);
        saveInstagram(arrayComment);
        fileComponent.exportJson(array, crawlerJsonSavePath, INSTAGRAM + "_" + "explore");
        fileComponent.exportJson(arrayComment, crawlerJsonSavePath, INSTAGRAM + "_COMMENT_" + "explore");
        return array;
    }

    private void saveInstagram(JSONArray jsonArray) throws Exception{
        Instagram instagram = new Instagram();
        for(Object jsonObject: jsonArray){
            instagramRepository.save(instagram.toEntity((JSONObject)jsonObject));
        }
    }

    
    /**
     * (tag) 키워드 정보로 검색하여 반환합니다.
     * @param jsonObject
     * @return JSONObject
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private JSONArray requestTagSearch(String keyword) throws Exception{
        Set<String> requestUrlSet = new LinkedHashSet<String>();
        try{
            driverRequestAndWait(tagUrl+tagUri+keyword);
            requestUrlSet = scrollDownRequestAndWaitAndGetUrl(requestUrlSet);
        }catch(Exception e){
            log("<EXCEPTION> can not request and wait process");
            return null;
        }

        JSONArray array = new JSONArray();
        JSONArray arrayComment = new JSONArray();
        
        for(String requestUrl : requestUrlSet){
            try{
                driverRequestAndWait(requestUrl);
                array.add(requestTagSearchDetail(requestUrl, keyword));
                arrayComment = scrapCommentList(arrayComment, requestUrl, keyword);
            }catch(Exception e){
                log("exception : " + requestUrl);
                continue;
            }
        }
        
        fileComponent.exportJson(array, crawlerJsonSavePath, INSTAGRAM + "_" + keyword);
        fileComponent.exportJson(arrayComment, crawlerJsonSavePath, INSTAGRAM + "_COMMENT_" + keyword);
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
        WebElement mainArea = driverCall().findElement(By.tagName("main"));
        WebElement contentArea = driverCall().findElement(By.cssSelector("ul._a9z6._a9za"));
        WebElement mainContent = contentArea.findElement(By.tagName("div"));
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
            author      = mainArea
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
     * 현재 드라이버 페이지의 댓글(인스타) 리스트를 가져온다.
     * @param jsonArray
     * @return
     * @throws Exception
     */
    private JSONArray scrapCommentList(JSONArray jsonArray, String url, String keyword) throws Exception{

        WebElement contentArea = driverCall().findElement(By.cssSelector("ul._a9z6._a9za"));
        List<WebElement> commentWebElementList = contentArea.findElements(By.tagName("ul"));
        // JSONObject 데이터 할당을 위한 변수 선언
        String regDate  =   null;
        String content  =   null;
        String title    =   null;
        String author   =   null;

        for(WebElement we : commentWebElementList){
            

            // 댓글에 해당하는 클래스 이외에는 로직처리 안함
            if(!we.getAttribute("class").contains("_a9ym")) continue;
            
            // 할당 전 변수 초기화
            regDate     =   null;
            content     =   null;
            title       =   null;
            author      =   null;

            // 작성 일시
            try{
                regDate      = we.findElement(By.tagName("time")).getAttribute("datetime");
            }catch(Exception e){
                log("<EXCEPTION> JSONObject Allocated fail from regdate : " + url);
            }

            try{
                // 작성 내용(추가 태그 포함)
                content      = we.getText();
                // 작성 내용 중 첫번째 줄 기준 최대 30자까지를 제목으로 지정한다.
                title        = content.substring(0,30);
            }catch(Exception e){
                log("<EXCEPTION> JSONObject Allocated fail from content : " + url);
            }

            // 작성자 ID
            try{
                author      = we.findElement(By.cssSelector("h3._a9zc")).getText();
            }catch(Exception e){
                log("<EXCEPTION> JSONObject Allocated fail from author : " + url);
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
            log("<PROCESS> JSONObject[comment] Allocated : " + url);
            jsonArray.add(hashMapToJsonObject(hashMap));
        }
        return jsonArray;
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

    private Set scrollDownRequestAndWaitAndGetUrl(Set<String> requestUrlSet) throws Exception{
        log("<PROCESS> content page scroll down execute");
        boolean success     = false;
        int retries         = 0;
        long currentDate    = 0;
        int checkRetries    = 0;
        int beforeSetCount  = 0;
        while (!success && retries < maxTryCount) {
            try {
                while(requestUrlSet.size() < CONTENTS_SCRAP_MAX && checkRetries < CONTENTS_SCRAP_TRY_MAX){
                    try{
                        log("<PROCESS> current request URL Set Scraped Size : "+ requestUrlSet.size() + " / request retry count : " + checkRetries);
                        currentDate = new Date().getTime();
                        while (new Date().getTime() < currentDate + DRIVER_TIME_OUT) { 
                            Thread.sleep(1000);
                            ((JavascriptExecutor)driverCall()).executeScript("window.scrollTo(0, document.body.scrollHeight)", driverCall().findElement(By.tagName("main")));
                            log("<PROCESS> content page scroll down");
                        }
                        List<WebElement> targetTagWebElementList = driverFindElement("main").findElements(By.tagName("a"));
                        for(WebElement we : targetTagWebElementList){
                            requestUrlSet.add(we.getAttribute("href"));
                        }
                        if(beforeSetCount == requestUrlSet.size()){
                            checkRetries++;
                        }else{
                            checkRetries = 0;
                        }
                        beforeSetCount = requestUrlSet.size();
                    }catch(Exception e){
                        driverCall().navigate().refresh();
                        continue;
                    }
                }
                success = true;
            } catch (Exception e) {
                log("<EXCEPTION> content page loaded failed and request count : " + retries++);
                continue;
            }
        }
        return requestUrlSet;
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
