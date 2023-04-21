package com.dailydatahub.dailydatacrawler.crawl.youtube.service;

import java.util.HashMap;
import java.net.URI;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.stereotype.Service;

import com.dailydatahub.dailydatacrawler.module.AESCompoent;
import com.dailydatahub.dailydatacrawler.module.FileComponent;
import com.dailydatahub.dailydatacrawler.module.JsonComponent;
import com.dailydatahub.dailydatacrawler.module.MD5Component;
import com.dailydatahub.dailydatacrawler.module.SeleniumComponent;
import com.dailydatahub.dailydatacrawler.module.RestComponent;
@Service
public class YoutubeServiceImpl implements YoutubeService{

    @Autowired
    public SeleniumComponent seleniumComponent;

    @Autowired
    public JsonComponent jsonComponent;

    @Autowired
    public RestComponent RestComponent;

    @Autowired
    public FileComponent fileComponent;

    @Autowired
    public AESCompoent aes;

    @Autowired
    public MD5Component md5;

    @Value("${external.youtube.url}")
    private String youtubeUrl;

    @Value("${external.youtube.api}")
    private String API_KEY;

    @Value("${external.youtube.search.uri}")
    private String searchUri;

    @Value("${external.youtube.comment.uri}")
    private String commentUri;

    @Value("${external.youtube.detail.uri}")
    private String detailUri;

    @Value("${crawler.json.save.path}")
    private String crawlerJsonSavePath;

    private String YOUTUBE = "YOUTUBE";


    /**
     * 검색합니다.
     */
    @Override
    public JSONArray search(String keyword) throws Exception {
        return requestInterface("search", keyword);
    }

    /**
     * 실시간 데이터를 검색합니다.
     */
    @Override
    public JSONArray explore() throws Exception {
        return requestInterface("explore", null);
    }

    /**
     * 실시간 데이터 현재 대상 카운트를 검색합니다.
     */
    @Override
    public JSONArray exploreCount() throws Exception {
        return requestInterface("exploreCount", null);
    }

    /**
     * 검검색 결과를 반환합니다.
     * @param requestType
     * @param keyword
     * @return JSONObject
     * @throws Exception
     */
    private JSONArray requestInterface(String requestType, String keyword) throws Exception{
        JSONArray array = new JSONArray();
        switch(requestType){
            case "search":array = requestKeywordSearch(keyword);
                break;
            case "explore":array = requestKeywordSearch(keyword);
                break;
            case "exploreCount":array = requestKeywordSearchCount();
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
    private JSONArray requestKeywordSearchCount() throws Exception{
        // build url
        String requestUrl = youtubeUrl + searchUri + "?maxResults=50&type=video&key=" + aes.decryptApiKey(API_KEY);
        URI uri = UriComponentsBuilder
                .fromUriString(requestUrl)
                .encode()
                .build()
                .toUri();
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(RestComponent.getRequest(uri));
        JSONArray jsonArray = (JSONArray)parser.parse(json.get("items").toString());
        log("<PROCESS> JSONObject Allocated : " + requestUrl);
        return jsonArray;
    }

    /**
     * (tag) 키워드 정보로 검색하여 반환합니다.
     * @param jsonObject
     * @return JSONObject
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private JSONArray requestKeywordSearch(String keyword) throws Exception{
        // build url
        String requestUrl = youtubeUrl + searchUri + "?maxResults=50&type=video&q=" + keyword + "&key=" + aes.decryptApiKey(API_KEY);
        if(keyword == null){
            requestUrl = youtubeUrl + searchUri + "?maxResults=50&type=video&key=" + aes.decryptApiKey(API_KEY);
            keyword="explore";
        }
        URI uri = UriComponentsBuilder
                .fromUriString(requestUrl)
                .encode()
                .build()
                .toUri();
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(RestComponent.getRequest(uri));
        JSONArray jsonArray = (JSONArray)parser.parse(json.get("items").toString());
        JSONArray jsonCommentArray = new JSONArray();
        for(int i = 0; i < jsonArray.size(); i++){
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            jsonCommentArray = requestKeywordSearchDetail(jsonCommentArray, ((JSONObject)jsonObject.get("id")).get("videoId").toString(),keyword);
        }
        fileComponent.exportJson(jsonArray, crawlerJsonSavePath, YOUTUBE + "_" + keyword);
        fileComponent.exportJson(jsonCommentArray, crawlerJsonSavePath, YOUTUBE + "_" + keyword + "_COMMENT");
        return jsonArray;
    }

    /**
     * 상세 정보를 가져옵니다.
     * @param url
     * @return JSONObject
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private JSONArray requestKeywordSearchDetail(JSONArray jsonCommentArray, String videoId, String keyword) throws Exception{
        String category                 = keyword;
        String title                    = null;
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        JSONParser parser               = new JSONParser();
        JSONObject json                 = new JSONObject();
        JSONArray jsonArray             = new JSONArray();
        String requestVideoDetailUrl    = youtubeUrl + detailUri + "?part=snippet&id=" + videoId + "&key=" + aes.decryptApiKey(API_KEY);
        URI uri = null;
        uri     = UriComponentsBuilder
                .fromUriString(requestVideoDetailUrl)
                .encode()
                .build()
                .toUri();
        json = (JSONObject) parser.parse(RestComponent.getRequest(uri));
        json = ((JSONObject)((JSONArray) parser.parse(json.get("items").toString())).get(0));
        json = (JSONObject) json.get("snippet");
        title = json.get("title").toString();
        log("<PROCESS> JSONObject Allocated : " + requestVideoDetailUrl);
        String requestUrl = youtubeUrl + commentUri + "?part=snippet&maxResults=100&videoId=" + videoId + "&key=" + aes.decryptApiKey(API_KEY);
        uri = UriComponentsBuilder
                .fromUriString(requestUrl)
                .encode()
                .build()
                .toUri();
        try{
            json = (JSONObject) parser.parse(RestComponent.getRequest(uri));
            jsonArray = (JSONArray)parser.parse(json.get("items").toString());
            for(int i = 0; i < jsonArray.size(); i++){
                JSONObject commentObject = ((JSONObject)((JSONObject)jsonArray.get(i)).get("snippet"));
                JSONObject commentTopJsonObject = ((JSONObject) ((JSONObject) commentObject.get("topLevelComment") ).get("snippet") );
                hashMap.clear();
                hashMap.put("_id",      md5.md5AndHex("https://www.youtube.com/watch?v=" + videoId));
                hashMap.put("snsId",    md5.md5AndHex("https://www.youtube.com/watch?v=" + videoId + commentTopJsonObject.get("updatedAt").toString()));
                hashMap.put("url",      "https://www.youtube.com/watch?v=" + videoId);
                hashMap.put("category", category);
                hashMap.put("press",    YOUTUBE);
                hashMap.put("regDate",  commentTopJsonObject.get("updatedAt").toString());
                hashMap.put("author",   commentTopJsonObject.get("authorDisplayName").toString());
                hashMap.put("content",  commentTopJsonObject.get("textOriginal").toString());
                hashMap.put("title",    title);
                hashMap.put("status",   true);
                log("<PROCESS> JSONObject Allocated : " + "https://www.youtube.com/watch?v=" + videoId);
                jsonCommentArray.add(hashMapToJsonObject(hashMap));
            }
        }catch(Exception e){
            log("<EXCEPTION>requestKeywordSearchDetailComment : " + jsonArray.size());
        }
        return jsonCommentArray;
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

    private void log(Object obj) throws Exception{
        System.out.println(obj);
    }

}
