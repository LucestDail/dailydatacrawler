package com.dailydatahub.dailydatacrawler.crawl.dcinside.service;

import org.json.simple.JSONArray;

public interface DcinsideService {
    public JSONArray search(String keyword) throws Exception;

    public JSONArray explore() throws Exception;
}
