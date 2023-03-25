package com.dailydatahub.dailydatacrawler.selenium.service;

import org.json.simple.JSONObject;

public interface SeleniumService {

    public JSONObject getUrlPage(String strUrl) throws Exception;

    public JSONObject putMethod(JSONObject jsonRequestObject) throws Exception;

    public JSONObject selectData(JSONObject jsonRequestObject);

    public JSONObject deleteData(JSONObject jsonRequestObject);

    public JSONObject insertData(JSONObject jsonRequestObject);
    
}
