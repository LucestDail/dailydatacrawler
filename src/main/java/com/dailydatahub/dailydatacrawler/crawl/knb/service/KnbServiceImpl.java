package com.dailydatahub.dailydatacrawler.crawl.knb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.dailydatahub.dailydatacrawler.module.JsoupComponent;
import com.dailydatahub.dailydatacrawler.module.SeleniumComponent;

@Service
public class KnbServiceImpl implements KnbService {

    @Value("${external.knb.api.url}")
    private String knbUrl;

    @Value("${external.knb.api.url.targetElement}")
    private String targetElement;

    @Autowired
    public JsoupComponent jsoupComponent;

    @Autowired
    public SeleniumComponent seleniumComponent;

    @Override
    public String getExchangeRate() throws Exception {
        return seleniumComponent.requestUrlById(knbUrl, targetElement);
    }

}
