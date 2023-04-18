package com.dailydatahub.dailydatacrawler.crawl.youtube.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dailydatahub.dailydatacrawler.crawl.youtube.service.YoutubeService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/youtube")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class YoutubeController {
    
    @Autowired
    YoutubeService service;

    /**
     * get youtube Search and list of comment
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
     * get youtube Search and list of comment
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

    /**
     * get youtube Search and list of comment
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/explore/count", method = RequestMethod.GET)
    @ResponseBody
    public JSONArray exploreCount() throws Exception {
        return service.exploreCount();

    }
}
