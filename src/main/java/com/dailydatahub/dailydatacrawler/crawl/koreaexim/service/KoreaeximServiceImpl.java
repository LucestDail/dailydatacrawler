package com.dailydatahub.dailydatacrawler.crawl.koreaexim.service;

import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import com.dailydatahub.dailydatacrawler.module.RestComponent;

@Service
public class KoreaeximServiceImpl implements KoreaeximService {

    @Value("${external.koreaexim.api.url}")
    private String url;

    @Value("${external.koreaexim.api.name}")
    private String name;

    @Value("${external.koreaexim.api.key}")
    private String apiKey;

    @Override
    public String getKoreaeximExchangeRate(String date) throws Exception {
        // https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=RpQ2hjrXVsPZN5IDSlMum5Lwm7VwKUot&searchdate=20220811&data=AP01
        URI uri = UriComponentsBuilder
                .fromUriString("https://www.koreaexim.go.kr")
                .path("/site/program/financial/exchangeJSON")
                .queryParam("authkey", apiKey)
                .queryParam("searchdate", date)
                .queryParam("data", "AP01")
                .encode()
                .build()
                .toUri();
        RestComponent rc = new RestComponent();
        return rc.getRequest(uri);
    }

}
