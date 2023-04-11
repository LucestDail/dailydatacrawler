package com.dailydatahub.dailydatacrawler.crawl.instagram.service;

import org.json.simple.JSONObject;

public class InstagramServiceImpl implements InstagramService{

    @Override
    public JSONObject getData() throws Exception {
        return requestAPI();
    }

    private JSONObject requestAPI() throws Exception{
        String requestKey = "";


        // implement instagram api data


        return new JSONObject();
    }
    
}
