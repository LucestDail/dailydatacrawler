package com.dailydatahub.dailydatacrawler.crawl.woori.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dailydatahub.dailydatacrawler.crawl.woori.service.WooriService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/woori")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class WooriController {

    @Autowired
    private WooriService wooriService;

    /**
     * get woori bank ExchangeRate
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{date}", method = RequestMethod.GET)
    @ResponseBody
    public String getExchangeRate() throws Exception {
        return wooriService.getExchangeRate();

    }

}
