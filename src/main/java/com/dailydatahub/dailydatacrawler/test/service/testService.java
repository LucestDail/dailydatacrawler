package com.dailydatahub.dailydatacrawler.test.service;

import org.json.simple.JSONObject;

public interface testService {
    
    public JSONObject selectData(String id) throws Exception;

    public JSONObject selectData(JSONObject jsonObjectRequestData) throws Exception;

    public JSONObject updateData(JSONObject jsonObjectRequestData) throws Exception;

    public JSONObject deleteData(JSONObject jsonObjectRequestData) throws Exception;

}
