package com.dailydatahub.dailydatacrawler.crawl.kebhana.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dailydatahub.dailydatacrawler.crawl.kebhana.service.KebhanaService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/kebhana")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class KebhanaController {

    @Autowired
    private final KebhanaService kebhanaService;

    /**
     * get Kebhana ExchangeRate
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/today", method = RequestMethod.GET)
    @ResponseBody
    public String getKoreaeximExchangeRate() throws Exception {
        return kebhanaService.getKebhanaExchangeRate();

    }
}
