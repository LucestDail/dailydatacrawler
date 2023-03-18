package com.dailydatahub.dailydatacrawler.crawl.sc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dailydatahub.dailydatacrawler.crawl.sc.service.ScService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sc")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ScController {

    @Autowired
    private final ScService scService;

    /**
     * get Kb ExchangeRate
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/today", method = RequestMethod.GET)
    @ResponseBody
    public String getExchangeRate() throws Exception {
        return scService.getExchangeRate();

    }
}
