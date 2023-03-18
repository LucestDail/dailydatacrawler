package com.dailydatahub.dailydatacrawler.repository.domain;

import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@JsonIgnoreProperties
@JsonPOJOBuilder
@SuppressWarnings({ "unchecked", "unused" })
public class DataObject {

    private JSONObject jsonObject;

    Map<String, Object> header;
    Map<String, Object> body;

    @Builder
    public DataObject() {
        jsonObject = new JSONObject();
        JSONObject objJsonHeader = new JSONObject();
        JSONArray objJsonDataArray = new JSONArray();
        objJsonHeader.put("ERROR_FLAG", false);
        objJsonHeader.put("ERROR_CODE", "");
        objJsonHeader.put("ERROR_MSG", "");
        objJsonDataArray.add(new JSONObject());
        jsonObject.put("HEADER", objJsonHeader);
        jsonObject.put("DATA", objJsonDataArray);
    }

    public JSONObject getJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("header", this.header);
        jsonObject.put("body", this.body);
        return jsonObject;
    }

    public boolean isError() {
        return (boolean) this.header.get("errorFlag");
    }

    public String getErrorMsg() {
        return (String) this.header.get("errorMsg");
    }

}
