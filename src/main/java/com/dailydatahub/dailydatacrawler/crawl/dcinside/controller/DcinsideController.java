package com.dailydatahub.dailydatacrawler.crawl.dcinside.controller;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dailydatahub.dailydatacrawler.crawl.dcinside.service.DcinsideService;

import org.springframework.web.bind.annotation.RequestMethod;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/dcinside")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DcinsideController {
    @Autowired
    private final DcinsideService dcinsideService;

    /**
     * get dcinside search keyword data bia 10 page
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/search/{keyword}", method = RequestMethod.GET)
    @ResponseBody
    public JSONArray search(@PathVariable("keyword") String keyword) throws Exception {
        return dcinsideService.search(keyword);
    }

     /**
     * get dcinside explore best contents bia 1 page
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/explore", method = RequestMethod.GET)
    @ResponseBody
    public JSONArray explore() throws Exception {
        return dcinsideService.explore();
    }
}