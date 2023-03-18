package com.dailydatahub.dailydatacrawler.module;

import java.net.URI;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestComponent {

    public String getRequest(URI uri) throws Exception {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 타임아웃 설정 5초
        factory.setConnectionRequestTimeout(5000);
        RestTemplate restTemplate = new RestTemplate(factory);

        HttpHeaders header = new HttpHeaders();
        HttpEntity<?> entity = new HttpEntity<>(header);

        ResponseEntity<String> responseEntityResult = restTemplate.exchange(uri.toString(), HttpMethod.GET, entity,
                String.class);
        return responseEntityResult.getBody();
    }

}
