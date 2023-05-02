package com.dailydatahub.dailydatacrawler.crawl.instagram.service;

import org.json.simple.JSONArray;

public interface InstagramService {

    public JSONArray tags(String tags) throws Exception;

    public JSONArray explore() throws Exception;

    public JSONArray exploreSave() throws Exception;
    
}
