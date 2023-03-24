package com.dailydatahub.dailydatacrawler.news.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dailydatahub.dailydatacrawler.news.dao.NewsRaw;
import com.dailydatahub.dailydatacrawler.news.dto.NewsRawDto;
import com.dailydatahub.dailydatacrawler.news.service.NewsService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {

    @Autowired
    NewsService service;

    /**
     * select specific domain list
     * 
     * @param news  select news category
     * @return
     */
    @RequestMapping(value = "/{domain}", method = RequestMethod.GET)
    public List<NewsRaw> getNewsRaws(@PathVariable("domain") String domain) {
        return service.getNewsRaws(domain);
    }

    /**
     * select detail
     * 
     * @param news  select news
     * @param id    select id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public NewsRaw getNewsRawsList(@PathVariable("id") String id) {
        return service.getNewsRawsDetail(id);
    }

    /**
     * insert 'dto'
     * where 'request'
     * 
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public boolean putNewsRaws(@RequestBody NewsRawDto dto, HttpServletRequest request) {
        return service.putNewsRaws(dto, request);
    }

    /**
     * update
     * where 'id'
     * and 'request'
     * set 'dto'
     * 
     * @param dto
     * @param request
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public boolean postNewsRaws(@RequestBody NewsRawDto dto, HttpServletRequest request,
            @PathVariable("id") String id) {
        return service.postNewsRaws(id, dto, request);
    }

    /**
     * delete
     * where 'id'
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public boolean deleteNewsRaws(@PathVariable("id") String id) {
        return service.deleteNewsRaws(id);
    }

}
