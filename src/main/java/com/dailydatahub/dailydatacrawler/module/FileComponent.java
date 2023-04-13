package com.dailydatahub.dailydatacrawler.module;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class FileComponent {

    public void exportJson(JSONObject json, String filePath, String fileName) throws Exception {
        FileWriter writer = new FileWriter(filePath + "/" + fileName + "_" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()) + ".json");
        try {
            writer.write(json.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            writer.close();
        }
    }

    public void exportJson(JSONArray json, String filePath, String fileName) throws Exception {
        FileWriter writer = new FileWriter(filePath + "/" + fileName + "_" + DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now()) + ".json");
        try {
            writer.write(json.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            writer.close();
        }
    }
}
