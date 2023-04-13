package com.dailydatahub.dailydatacrawler.crawl.instagram.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public interface InstagramService {

    public JSONArray tags(String tags) throws Exception;
    
}
