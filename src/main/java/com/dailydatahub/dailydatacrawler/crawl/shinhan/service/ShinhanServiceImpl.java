package com.dailydatahub.dailydatacrawler.crawl.shinhan.service;

import org.springframework.stereotype.Service;
import com.dailydatahub.dailydatacrawler.module.SeleniumComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Service
public class ShinhanServiceImpl implements ShinhanService {

    @Value("${external.shinhan.api.url}")
    private String url;

    @Value("${external.shinhan.api.url.targetElement}")
    private String targetElement;

    @Autowired
    public SeleniumComponent seleniumComponent;

    @Override
    public String getExchangeRate() throws Exception {
        return seleniumComponent.requestUrlById(url, targetElement);
    }

}
