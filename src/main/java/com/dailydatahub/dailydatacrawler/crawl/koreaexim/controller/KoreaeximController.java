package com.dailydatahub.dailydatacrawler.crawl.koreaexim.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dailydatahub.dailydatacrawler.crawl.koreaexim.service.KoreaeximService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/koreaexim")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class KoreaeximController {

    @Autowired
    private final KoreaeximService koreaeximService;

    /**
     * get Koreaexim ExchangeRate
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{date}", method = RequestMethod.GET)
    @ResponseBody
    public String getKoreaeximExchangeRate(@PathVariable("date") String date) throws Exception {
        return koreaeximService.getKoreaeximExchangeRate(date);

    }
}
