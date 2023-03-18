package com.dailydatahub.dailydatacrawler.crawl.shinhan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dailydatahub.dailydatacrawler.crawl.shinhan.service.ShinhanService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/shinhan")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ShinhanController {

    @Autowired
    private final ShinhanService shinhanService;

    /**
     * get Shinhan ExchangeRate
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/today", method = RequestMethod.GET)
    @ResponseBody
    public String getExchangeRate() throws Exception {
        return shinhanService.getExchangeRate();

    }
}
