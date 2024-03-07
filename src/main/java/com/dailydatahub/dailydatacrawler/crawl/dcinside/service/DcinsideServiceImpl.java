package com.dailydatahub.dailydatacrawler.crawl.dcinside.service;

import java.time.Year;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dailydatahub.dailydatacrawler.crawl.dcinside.dao.domain.Dcinside;
import com.dailydatahub.dailydatacrawler.crawl.dcinside.dao.repository.DcinsideRepository;
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
    private JsoupComponent jsoupComponent;

    @Autowired
    private SeleniumComponent seleniumComponent;

    @Autowired
    private MD5Component md5;

    @Autowired
    private FileComponent fileComponent;

    @Autowired
    private DcinsideRepository dcinsideRepository;

    @Override
    public JSONArray search(String keyword) throws Exception {


        // 대상 url 정보를 가져옵니다.
        Set<String> requestUrlSet = new LinkedHashSet<String>();
        try{
            driverRequestAndWait(dcinsideUrl + keyword);
            requestUrlSet = getUrlSetSearch(requestUrlSet);
        }catch(Exception e){
            e.printStackTrace();
            log("<EXCEPTION> can not request and wait process");
            return null;
        }

        JSONArray array = new JSONArray();
        JSONArray arrayComment = new JSONArray();
        for(String requestUrl : requestUrlSet){
            try{
                 // 대상 url 을 접근합니다.
                driverRequestAndWait(requestUrl);
                // 대상 본문 정보와 댓글 정보를 가져옵니다.
                log("<PROCESS> scrap content");
                array = scrapContentList(array, requestUrl, keyword);
                log("<PROCESS> scrap comment list");
                arrayComment = scrapCommentList(arrayComment, requestUrl, keyword);
            }catch(Exception e){
                log("exception : " + requestUrl);
                continue;
            }
        }
        saveDcinside(array);
        saveDcinside(arrayComment);
        return array;
    }

    @Override
    public JSONArray explore() throws Exception {

        // 대상 url 정보를 가져옵니다.
        Set<String> requestUrlSet = new LinkedHashSet<String>();
        try{
            driverRequestAndWait(dcinsideBestUrl);
            requestUrlSet = getUrlSetExplore(requestUrlSet);
        }catch(Exception e){
            e.printStackTrace();
            log("<EXCEPTION> can not request and wait process");
            return null;
        }

        JSONArray array = new JSONArray();
        JSONArray arrayComment = new JSONArray();
        for(String requestUrl : requestUrlSet){
            try{
                 // 대상 url 을 접근합니다.
                driverRequestAndWait(requestUrl);
                // 대상 본문 정보와 댓글 정보를 가져옵니다.
                log("<PROCESS> scrap content");
                array = scrapContentList(array, requestUrl, "explore");
                log("<PROCESS> scrap comment list");
                arrayComment = scrapCommentList(arrayComment, requestUrl, "explore");
            }catch(Exception e){
                log("exception : " + requestUrl);
                continue;
            }
        }
        saveDcinside(array);
        saveDcinside(arrayComment);
        return array;
    }

    /**
     * get all of data from instagram explore tab
     */
    @Override
    public JSONArray exploreSave() throws Exception {
        // 대상 url 정보를 가져옵니다.
        Set<String> requestUrlSet = new LinkedHashSet<String>();
        try{
            driverRequestAndWait(dcinsideBestUrl);
            requestUrlSet = getUrlSetExplore(requestUrlSet);
        }catch(Exception e){
            e.printStackTrace();
            log("<EXCEPTION> can not request and wait process");
            return null;
        }

        JSONArray array = new JSONArray();
        JSONArray arrayComment = new JSONArray();
        for(String requestUrl : requestUrlSet){
            try{
                 // 대상 url 을 접근합니다.
                driverRequestAndWait(requestUrl);
                // 대상 본문 정보와 댓글 정보를 가져옵니다.
                log("<PROCESS> scrap content");
                array = scrapContentList(array, requestUrl, "explore");
                log("<PROCESS> scrap comment list");
                arrayComment = scrapCommentList(arrayComment, requestUrl, "explore");
            }catch(Exception e){
                log("exception : " + requestUrl);
                continue;
            }
        }
        saveDcinside(array);
        saveDcinside(arrayComment);
        return array;
    }

    private void saveDcinside(JSONArray jsonArray) throws Exception{
        Dcinside dcinside = new Dcinside();
        for(Object jsonObject: jsonArray){
            try{
                dcinsideRepository.save(dcinside.toEntity((JSONObject)jsonObject));
            }catch(Exception e){
                e.printStackTrace();
            }
            
        }
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

    private Set getUrlSetSearch(Set<String> requestUrlSet) throws Exception{
        log("<PROCESS> content page scroll down execute");
        WebElement webElement = driverCall().findElement(By.tagName("body"));
        WebElement targetElement = webElement.findElement(By.className(dcinsideTargetElemenString));
        List<WebElement> listWebElement = targetElement.findElements(By.tagName("a"));
        for(WebElement we : listWebElement){
            if(we.getAttribute("href").contains("view")){
                requestUrlSet.add(we.getAttribute("href"));
            }
        }
        return requestUrlSet;
    }

    private Set getUrlSetExplore(Set<String> requestUrlSet) throws Exception{
        log("<PROCESS> content page scroll down execute");
        WebElement webElement = driverCall().findElement(By.tagName("body"));
        WebElement targetElement = webElement.findElement(By.tagName("tbody"));
        List<WebElement> listWebElement = targetElement.findElements(By.cssSelector("tr.ub-content.us-post"));
        for(WebElement we : listWebElement){
            requestUrlSet.add(we.findElement(By.cssSelector("td.gall_tit.ub-word")).findElements(By.tagName("a")).get(0).getAttribute("href"));
        }
        return requestUrlSet;
    }

    private JSONArray scrapContentList(JSONArray jsonArray, String requestUrl, String keyword) throws Exception{
        jsonArray.add(requestTagSearchDetail(requestUrl, keyword));
        return jsonArray;
    }

    private JSONArray scrapCommentList(JSONArray jsonArray, String requestUrl, String keyword) throws Exception{
        return requestTagSearchDetailComment(jsonArray, requestUrl, keyword);
    }

    /**
     * (tag)url 정보로 접속하여 상세 정보를 가져옵니다.
     * @param url
     * @return JSONObject
     * @throws Exception
     */
    private JSONObject requestTagSearchDetail(String url, String keyword) throws Exception{

        // 드라이버 브라우저에서 필요한 내용을 찾습니다.
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
        log("<PROCESS> JSONObject Allocated : " + url);
        System.out.println(content);
        return hashMapToJsonObject(hashMap);
    }

    /**
     * (tag)url 정보로 접속하여 상세 정보의 댓글 정보를 가져옵니다.
     * @param url
     * @return JSONObject
     * @throws Exception
     */
    private JSONArray requestTagSearchDetailComment(JSONArray jsonArray, String url, String keyword) throws Exception{

        // 드라이버 브라우저에서 필요한 내용을 찾습니다.
        WebElement mainContent = driverFindElementCSSSelector("ul.cmt_list");
        List<WebElement> commentWebElementList = mainContent.findElements(By.cssSelector("li.ub-content"));

        // JSONObject 데이터 할당을 위한 변수 선언
        String regDate          =   null;
        String content          =   null;
        String title            =   null;
        String author           =   null;
        String authorIp         = null;
        String authorNickName   = null;
        String authorId         =  null;
        log("<PROCESS> requestTagSearchDetailComment looping count : " + commentWebElementList.size());
        for(WebElement we : commentWebElementList){
            // 할당 전 변수 초기화
            regDate     =   null;
            content     =   null;
            title       =   null;
            author      =   null;

            // 작성 일시
            try{
                regDate     = we.findElement(By.cssSelector("span.date_time")).getText();
                regDate     = String.valueOf(Year.now().getValue()) + "." + regDate;
            }catch(Exception e){
                log("<EXCEPTION> JSONObject Allocated fail from regdate : " + url);
            }

            try{
                // 작성 내용(추가 태그 포함)
                content      = we.getText();
                // 작성 내용 중 첫번째 줄 기준 최대 30자까지를 제목으로 지정한다.
                title        = driverFindElementID("container").findElement(By.className("view_content_wrap")).findElement(By.className("title_subject")).getText();
            }catch(Exception e){
                log("<EXCEPTION> JSONObject Allocated fail from content and title : " + url);
            }

            // 작성자 ID
            try{
                WebElement authorElement = we.findElement(By.cssSelector("span.gall_writer.ub-writer"));
                authorIp = authorElement.getAttribute("data-ip");
                authorNickName = authorElement.getAttribute("data-nick");
                authorId = authorElement.getAttribute("data-uid");
            }catch(Exception e){
                log("<EXCEPTION> autho information has failed");
            }
            author = authorNickName + "(" + authorId + "," + authorIp + ")";
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
            log("<PROCESS> JSONObject[comment] Allocated : " + url);
            System.out.println(content);
            jsonArray.add(hashMapToJsonObject(hashMap));
        }
        return jsonArray;
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

     /**
     * 현재 드라이버에서 HTML 태그 엘리먼트 ID를 검색합니다.
     * @param elementString
     * @return WebElement
     * @throws Exception
     */
    private WebElement driverFindElementCSSSelector(String elementString) throws Exception{
        return driverCall().findElement(By.cssSelector(elementString));
    }

    private void log(Object obj) throws Exception{
        System.out.println(obj);
    }

}
