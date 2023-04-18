package com.dailydatahub.dailydatacrawler.crawl.twitter.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public interface Twitterservice {

    public JSONArray search(String keyword) throws Exception;

    public JSONArray explore() throws Exception;
    
}
