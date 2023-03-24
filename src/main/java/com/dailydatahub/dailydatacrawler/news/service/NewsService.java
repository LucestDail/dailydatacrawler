package com.dailydatahub.dailydatacrawler.news.service;

import java.util.List;

import com.dailydatahub.dailydatacrawler.news.dao.NewsRaw;
import com.dailydatahub.dailydatacrawler.news.dto.NewsRawDto;

import jakarta.servlet.http.HttpServletRequest;

public interface NewsService {

    public List<NewsRaw> getNewsRaws(String News);

    public NewsRaw getNewsRawsDetail(String id);

    public boolean putNewsRaws(NewsRawDto dto, HttpServletRequest request);

    public boolean postNewsRaws(String id, NewsRawDto dto, HttpServletRequest request);

    public boolean deleteNewsRaws(String id);

}
