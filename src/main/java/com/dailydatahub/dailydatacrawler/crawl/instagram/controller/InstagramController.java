package com.dailydatahub.dailydatacrawler.crawl.instagram.controller;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.dailydatahub.dailydatacrawler.crawl.instagram.dao.domain.Instagram;
import com.dailydatahub.dailydatacrawler.crawl.instagram.dao.repositorty.InstagramRepository;
import com.dailydatahub.dailydatacrawler.crawl.instagram.service.InstagramService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/instagram")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class InstagramController {
    
    @Autowired
    private InstagramService service;

    @Autowired
    private InstagramRepository instagramRepository;

    /**
     * get Instagram Tag Search
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/tags/{tags}", method = RequestMethod.GET)
    @ResponseBody
    public JSONArray tags(@PathVariable("tags") String tags) throws Exception {
        return service.tags(tags);

    }

    /**
     * get Instagram get all of current explore tab
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/explore", method = RequestMethod.GET)
    @ResponseBody
    public JSONArray tags() throws Exception {
        return service.explore();

    }

/**
     * get Instagram get all of current explore tab
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/exploreSave", method = RequestMethod.GET)
    @ResponseBody
    public JSONArray requestExploreSave() throws Exception {
        return service.exploreSave();

    }

    /**
     * get all list of instagram crawl data
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public JSONArray getAllInstagrams() {
        List<Instagram> instagrams = instagramRepository.findAll();
        JSONArray jsonArray = new JSONArray();
        for(Instagram instagram : instagrams){
            jsonArray.add(instagram.toJson());
        }
        return jsonArray;
    }

    /**
     * get specific "id" instagram crawl data
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JSONObject getInstagramById(@PathVariable Long id){
        Instagram instagram = instagramRepository
                                .findById(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return instagram.toJson();
    }

    /**
     * create specific "id" instagram crawl data
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public JSONObject createBook(@RequestBody JSONObject instagramJsonObject) {
        Instagram instagram = new Instagram().toEntity(instagramJsonObject);
        instagram = instagramRepository.save(instagram);
        return instagram.toJson();
    }

    /**
     * update specific "id" instagram crawl data which specific part of data
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public JSONObject updateInstagram(@PathVariable Long id, @RequestBody JSONObject instagramJsonObject) {
        Instagram instagram = new Instagram().toEntity(instagramJsonObject);
        instagram.setId(id);
        instagram = instagramRepository.save(instagram);
        return instagram.toJson();
    }

    /**
     * update specific "id" instagram crawl data which entire "id" data
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JSONObject updateInstagramAll(@PathVariable Long id, @RequestBody JSONObject instagramJsonObject) {
        Instagram instagram = new Instagram().toEntity(instagramJsonObject);
        instagram.setId(id);
        instagram = instagramRepository.save(instagram);
        return instagram.toJson();
    }

    /**
     * delete specific "id" instagram crawl data
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteBook(@PathVariable Long id) {
        instagramRepository.deleteById(id);
    }
}