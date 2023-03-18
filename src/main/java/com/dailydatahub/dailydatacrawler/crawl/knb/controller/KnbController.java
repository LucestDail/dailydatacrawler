package com.dailydatahub.dailydatacrawler.crawl.knb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dailydatahub.dailydatacrawler.crawl.knb.service.KnbService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/knb")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class KnbController {

    @Autowired
    private KnbService knbService;

    /**
     * get knb ExchangeRate
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{date}", method = RequestMethod.GET)
    @ResponseBody
    public String getExchangeRate() throws Exception {
        return knbService.getExchangeRate();

    }

}
