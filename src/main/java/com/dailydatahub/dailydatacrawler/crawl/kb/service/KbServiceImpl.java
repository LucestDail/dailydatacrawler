package com.dailydatahub.dailydatacrawler.crawl.kb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.dailydatahub.dailydatacrawler.module.JsoupComponent;

@Service
@Configurable
public class KbServiceImpl implements KbService {

    @Value("${external.kb.api.url}")
    private String kbUrl;

    @Value("${external.kb.api.url.targetElement}")
    private String targetElement;

    @Autowired
    public JsoupComponent jsoupComponent;

    @Override
    public String getExchangeRate() throws Exception {
        return jsoupComponent.jsoupRequeString(kbUrl).getElementById(targetElement).html();
    }

}
