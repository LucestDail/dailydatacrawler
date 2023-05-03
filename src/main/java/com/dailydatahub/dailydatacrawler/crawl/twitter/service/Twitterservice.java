package com.dailydatahub.dailydatacrawler.crawl.twitter.service;

import org.json.simple.JSONArray;

public interface Twitterservice {

    public JSONArray search(String keyword) throws Exception;

    public JSONArray explore() throws Exception;

    public JSONArray exploreSave() throws Exception;
    
}
