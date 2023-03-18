package com.dailydatahub.dailydatacrawler.crawl.woori.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.dailydatahub.dailydatacrawler.module.JsoupComponent;
import com.dailydatahub.dailydatacrawler.module.SeleniumComponent;

@Service
public class WooriServiceImpl implements WooriService {

    @Value("${external.woori.api.url}")
    private String wooriUrl;

    @Value("${external.woori.api.url.targetElement}")
    private String targetElement;

    @Autowired
    public JsoupComponent jsoupComponent;

    @Autowired
    public SeleniumComponent seleniumComponent;

    @Override
    public String getExchangeRate() throws Exception {
        return seleniumComponent.requestUrlById(wooriUrl, targetElement);
    }

}
