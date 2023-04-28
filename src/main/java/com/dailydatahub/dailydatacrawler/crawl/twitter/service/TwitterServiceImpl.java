package com.dailydatahub.dailydatacrawler.crawl.twitter.service;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

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
public class TwitterServiceImpl implements Twitterservice{

    @Autowired
    public SeleniumComponent seleniumComponent;

    @Autowired
    public JsonComponent jsonComponent;

    @Autowired
    public FileComponent fileComponent;

    @Autowired
    public MD5Component md5;

    @Value("${external.twitter.search.url}")
    private String searchUrl;

    @Value("${external.twitter.search.uri}")
    private String searchUri;

    @Value("${external.twitter.explore.uri}")
    private String exploreUri;

    @Value("${crawler.json.save.path}")
    private String crawlerJsonSavePath;

    @Value("${crawler.access.count.max}")
    private int maxTryCount;

    private String TWITTER = "TWITTER";
    private long DRIVER_TIME_OUT=5000l;
    private int CONTENTS_SCRAP_MAX = 100;
    private int CONTENTS_SCRAP_TRY_MAX = 10;

    /**
     * 태그 단위로 검색합니다.
     */
    @Override
    public JSONArray search(String keyword) throws Exception {
        return requestInterface("search", keyword);
    }

    /**
     * 실시간 트렌드 내부를 순회하여 검색합니다.
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
        switch(requestType){
            case "search":array = requestWordSearch(keyword);
                break;
            case "explore":array = requestExplore();
                break;
            default:
                break;
        }
        return array;
    }

    /**
     * (word) 키워드 정보로 검색하여 반환합니다.
     * @param jsonObject
     * @return JSONObject
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private JSONArray requestWordSearch(String keyword) throws Exception{

        Set<String> requestUrlSet = new LinkedHashSet<String>();

        try{
            driverRequestAndWait(searchUrl+searchUri+"?q="+keyword,DRIVER_TIME_OUT);
            requestUrlSet = scrollDownRequestAndWaitAndGetUrl(requestUrlSet);
        }catch(Exception e){
            log("<EXCEPTION> can not request and wait process");
            return null;
        }

        JSONArray array = new JSONArray();

        for(String requestUrl : requestUrlSet){
            try{
                array.add(requestWordSearchDetail(requestUrl, keyword));
            }catch(Exception e){
                log("exception : " + requestUrl);
                continue;
            }
        }

        fileComponent.exportJson(array, crawlerJsonSavePath, TWITTER + "_" + keyword);
        return array;
    }

    
    /**
     * (word) 키워드 정보로 검색하여 반환합니다.
     * @param jsonObject
     * @return JSONObject
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private JSONArray requestExplore() throws Exception{

        Set<String> requestUrlSet = new LinkedHashSet<String>();

        log("<PROCESS> access to content list page >>> " + searchUrl + exploreUri);
        try{
            driverRequestAndWait(searchUrl);
            requestUrlSet = scrollDownRequestAndWaitAndGetUrl(requestUrlSet);
        }catch(Exception e){
            log("<EXCEPTION> can not request and wait process");
            return null;
        }

        JSONArray array = new JSONArray();

        for(String requestUrl : requestUrlSet){
            try{
                array.add(requestWordSearchDetail(requestUrl, "explore"));
            }catch(Exception e){
                log("exception : " + requestUrl);
                continue;
            }
        }

        fileComponent.exportJson(array, crawlerJsonSavePath, TWITTER + "_" + "explore");
        return array;
    }

    /**
     * (tag)url 정보로 접속하여 상세 정보를 가져옵니다.
     * @param url
     * @return JSONObject
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private JSONObject requestWordSearchDetail(String url, String keyword) throws Exception{

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
            // 작성 내용
            for(WebElement webElement : mainContent.findElements(By.tagName("span"))){
                content += webElement.getText();
            }
            content     = content.replaceAll("\n", " ");
            // 작성 내용 중 첫번째 줄
            title        = content.substring(0,30);
        }catch(Exception e){
            log("content not Found");
        }

        // 작성자 ID
        try{
            author      = url.replaceAll("https://twitter.com/","").split("/status/")[0];
        }catch(Exception e){
            log("author not Found");
        }

        String category     = keyword;
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("_id",      md5.md5AndHex(url));
        hashMap.put("snsId",    md5.md5AndHex(url+regDate));
        hashMap.put("url",      url);
        hashMap.put("category", category);
        hashMap.put("press",    TWITTER);
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
    private void driverRequestAndWait(String url, Long waitPeriod) throws Exception{
        driverCall().get(url);
        Thread.sleep(waitPeriod);
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
                        
                        Pattern regex = Pattern.compile("https://twitter.com/[^/]+/status/[^/]+");
                        for(WebElement we : driverCall().findElement(By.tagName("main")).findElement(By.tagName("section")).findElements(By.tagName("a"))){
                            if (regex.matcher(we.getAttribute("href")).matches()){
                                requestUrlSet.add(we.getAttribute("href"));
                            }
                        }
                        if(beforeSetCount == requestUrlSet.size()){
                            checkRetries++;
                        }
                        beforeSetCount = requestUrlSet.size();
                        driverCall().navigate().refresh();
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
