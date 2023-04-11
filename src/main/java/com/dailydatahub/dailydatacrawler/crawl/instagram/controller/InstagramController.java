package com.dailydatahub.dailydatacrawler.crawl.instagram.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dailydatahub.dailydatacrawler.crawl.instagram.service.InstagramService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/instagram")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class InstagramController {
    
    @Autowired
    InstagramService service;

    /**
     * get Instagram open API Data
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/data", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getData() throws Exception {
        return service.getData();

    }
}
