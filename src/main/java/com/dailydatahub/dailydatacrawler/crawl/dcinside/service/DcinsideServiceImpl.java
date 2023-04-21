package com.dailydatahub.dailydatacrawler.crawl.dcinside.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dailydatahub.dailydatacrawler.module.FileComponent;
import com.dailydatahub.dailydatacrawler.module.JsoupComponent;
import com.dailydatahub.dailydatacrawler.module.MD5Component;
import com.dailydatahub.dailydatacrawler.module.SeleniumComponent;

@Service
@Configurable
public class DcinsideServiceImpl implements DcinsideService {

    @Value("${external.dcinside.api.url}")
    private String dcinsideUrl;

    @Value("${external.dcinside.api.urletc}")
    private String dcinsideUrlEtc;

    @Value("${external.dcinside.api.urlbest}")
    private String dcinsideBestUrl;

    @Value("${external.dcinside.api.url.targetElement}")
    private String dcinsideTargetElemenString;

    @Value("${crawler.access.count.max}")
    private int maxTryCount;

    @Value("${crawler.access.page.max}")
    private int maxPageCount;

    @Value("${crawler.timeout}")
    private int DRIVER_TIME_OUT;

    @Value("${crawler.json.save.path}")
    private String crawlerJsonSavePath;

    private String DCINSIDE = "DCINSIDE";

    @Autowired
    public JsoupComponent jsoupComponent;

    @Autowired
    public SeleniumComponent seleniumComponent;

    @Autowired
    public MD5Component md5;

    @Autowired
    public FileComponent fileComponent;

    @Override
    public JSONArray search(String keyword) throws Exception {
        JSONArray array = new JSONArray();
        for(int i = 0; i < maxPageCount; i++){
            driverRequestAndWait(dcinsideUrl + i + dcinsideUrlEtc + keyword);
            queryPageAndConvertJsonArray(array, keyword);
        }
        fileComponent.exportJson(array, crawlerJsonSavePath, DCINSIDE + "_" + "keyword");
        return array;
    }

    @Override
    public JSONArray explore() throws Exception {
        JSONArray array = new JSONArray();
        driverRequestAndWait(dcinsideBestUrl);
        queryBoardAndConvertJsonArray(array, "explore");
        fileComponent.exportJson(array, crawlerJsonSavePath, DCINSIDE + "_" + "explore");
        return array;
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


    private JSONArray queryBoardAndConvertJsonArray(JSONArray jsonArray, String keyword) throws Exception{
        WebElement webElement = driverCall().findElement(By.tagName("body"));
        WebElement targetElement = webElement.findElement(By.tagName("tbody"));
        List<WebElement> listWebElement = targetElement.findElements(By.cssSelector("tr.ub-content.us-post"));
        List<String> requestUrlList = new ArrayList<>();
        for(WebElement we : listWebElement){
            requestUrlList.add(we.findElement(By.cssSelector("td.gall_tit.ub-word")).findElements(By.tagName("a")).get(0).getAttribute("href"));
        }
        for(int i = 0; i < requestUrlList.size(); i++) {
            try{
                jsonArray.add(requestTagSearchDetail(requestUrlList.get(i), keyword));
            }catch(Exception e){
                e.printStackTrace();
                log("exception : " + requestUrlList.get(i));
                continue;
            }
        }
        return jsonArray;
    }

    private JSONArray queryPageAndConvertJsonArray(JSONArray jsonArray, String keyword)throws Exception{
        WebElement webElement = driverCall().findElement(By.tagName("body"));
        WebElement targetElement = webElement.findElement(By.className(dcinsideTargetElemenString));
        List<WebElement> listWebElement = targetElement.findElements(By.tagName("a"));
        List<String> requestUrlList = new ArrayList<>();
        for(WebElement we : listWebElement){
            if(we.getAttribute("href").contains("view")){
                requestUrlList.add(we.getAttribute("href"));
            }
        }
        for(int i = 0; i < requestUrlList.size(); i++) {
            try{
                jsonArray.add(requestTagSearchDetail(requestUrlList.get(i), keyword));
            }catch(Exception e){
                e.printStackTrace();
                log("exception : " + requestUrlList.get(i));
                continue;
            }
        }
        return jsonArray;
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
        WebElement mainContent = driverFindElementID("container");
        WebElement mainArticle = mainContent.findElement(By.className("view_content_wrap"));

        String regDate  =   mainArticle.findElement(By.className("gall_date")).getAttribute("title");
        String content  =   mainArticle.findElement(By.className("write_div")).getText();
        String title    =   mainArticle.findElement(By.className("title_subject")).getText();

        String authorIp = null;
        String authorNickName = null;
        String authorId   =  null;

        try{
            WebElement authorElement = mainArticle.findElement(By.tagName("header")).findElement(By.cssSelector("div.gall_writer.ub-writer"));
            authorIp = authorElement.getAttribute("data-ip");
            authorNickName = authorElement.getAttribute("data-nick");
            authorId = authorElement.getAttribute("data-uid");
        }catch(Exception e){
            log("<EXCEPTION> autho information has failed");
        }
        String author = authorNickName + "(" + authorId + "," + authorIp + ")";
        String category     = keyword;
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("_id",      md5.md5AndHex(url));
        hashMap.put("snsId",    md5.md5AndHex(url+regDate));
        hashMap.put("url",      url);
        hashMap.put("category", category);
        hashMap.put("press",    DCINSIDE);
        hashMap.put("regDate",  regDate);
        hashMap.put("author",   author);
        hashMap.put("content",  content);
        hashMap.put("title",    title);
        hashMap.put("status",   true);
        log(hashMap);
        return hashMapToJsonObject(hashMap);
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

    /**
     * 현재 드라이버에서 HTML 태그 엘리먼트 ID를 검색합니다.
     * @param elementString
     * @return WebElement
     * @throws Exception
     */
    private WebElement driverFindElementID(String elementString) throws Exception{
        return driverCall().findElement(By.id(elementString));
    }

    private void log(Object obj) throws Exception{
        System.out.println(obj);
    }

}
