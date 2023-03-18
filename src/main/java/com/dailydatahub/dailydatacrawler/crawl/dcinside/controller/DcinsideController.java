package com.dailydatahub.dailydatacrawler.crawl.dcinside.controller;

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
     * get Kb ExchangeRate
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{page}", method = RequestMethod.GET)
    @ResponseBody
    public String getExchangeRate(@PathVariable("page") String page) throws Exception {
        return dcinsideService.getPage(page);

    }
}