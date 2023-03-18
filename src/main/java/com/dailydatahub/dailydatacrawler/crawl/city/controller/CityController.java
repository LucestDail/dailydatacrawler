package com.dailydatahub.dailydatacrawler.crawl.city.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dailydatahub.dailydatacrawler.crawl.city.service.CityService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/city")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CityController {

    @Autowired
    private CityService cityService;

    /**
     * get city bank ExchangeRate
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{date}", method = RequestMethod.GET)
    @ResponseBody
    public String getExchangeRate() throws Exception {
        return cityService.getExchangeRate();

    }

}
