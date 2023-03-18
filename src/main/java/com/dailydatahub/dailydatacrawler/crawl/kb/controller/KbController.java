package com.dailydatahub.dailydatacrawler.crawl.kb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dailydatahub.dailydatacrawler.crawl.kb.service.KbService;

import org.springframework.web.bind.annotation.RequestMethod;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/kb")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class KbController {

    @Autowired
    private final KbService kbService;

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
        return kbService.getExchangeRate();

    }

}
