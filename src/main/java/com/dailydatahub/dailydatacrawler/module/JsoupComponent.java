package com.dailydatahub.dailydatacrawler.module;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class JsoupComponent {
    public Document jsoupRequeString(String url) throws Exception {
        Response execute = Jsoup.connect(url).execute();
        return Jsoup.parse(execute.body());
    }

}
