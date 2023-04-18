package com.dailydatahub.dailydatacrawler.crawl.twitter.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
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
     * get Twitter search word
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/search/{keyword}", method = RequestMethod.GET)
    @ResponseBody
    public JSONArray search(@PathVariable("keyword") String keyword) throws Exception {
        return service.search(keyword);
    }

    /**
     * get Twitter trend data
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/explore", method = RequestMethod.GET)
    @ResponseBody
    public JSONArray explore() throws Exception {
        return service.explore();

    }
}
