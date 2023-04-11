package com.dailydatahub.dailydatacrawler.crawl.twitter.service;

import org.json.simple.JSONObject;

public class TwitterServiceImpl implements Twitterservice{

    @Override
    public JSONObject getData() throws Exception {
        return requestAPI();
    }

    private JSONObject requestAPI() throws Exception{
        String requestKey = "";


        // implement twitter api data


        return new JSONObject();
    }
    
}
