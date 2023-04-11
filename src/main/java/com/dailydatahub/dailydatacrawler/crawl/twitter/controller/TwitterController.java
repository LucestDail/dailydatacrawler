package com.dailydatahub.dailydatacrawler.crawl.twitter.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dailydatahub.dailydatacrawler.crawl.twitter.service.Twitterservice;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/twitter")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TwitterController {
    
    @Autowired
    Twitterservice service;

    /**
     * get woori bank ExchangeRate
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
