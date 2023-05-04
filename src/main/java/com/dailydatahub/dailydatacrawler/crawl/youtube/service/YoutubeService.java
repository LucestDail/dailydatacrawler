package com.dailydatahub.dailydatacrawler.crawl.youtube.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public interface YoutubeService {

    public JSONArray search(String keyword) throws Exception;
    public JSONArray explore() throws Exception;
    public JSONArray exploreCount() throws Exception;
    public JSONArray exploreSave() throws Exception;
    
}
