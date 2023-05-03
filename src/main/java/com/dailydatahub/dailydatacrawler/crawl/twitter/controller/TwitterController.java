package com.dailydatahub.dailydatacrawler.crawl.twitter.controller;

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

import com.dailydatahub.dailydatacrawler.crawl.twitter.dao.domain.Twitter;
import com.dailydatahub.dailydatacrawler.crawl.twitter.dao.repository.TwitterRepository;
import com.dailydatahub.dailydatacrawler.crawl.twitter.service.Twitterservice;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/twitter")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TwitterController {
    
    @Autowired
    Twitterservice service;

    @Autowired
    private TwitterRepository twitterRepository;

    /**
     * get Twitter search word
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/search/{keyword}", method = RequestMethod.GET)
    @ResponseBody
    public JSONArray search(@PathVariable("keyword") String keyword) throws Exception {
        return service.search(keyword);
    }

    /**
     * get Twitter trend data
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/explore", method = RequestMethod.GET)
    @ResponseBody
    public JSONArray explore() throws Exception {
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
     * get all list of Twitter crawl data
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public JSONArray getAllTwitters() {
        List<Twitter> Twitters = twitterRepository.findAll();
        JSONArray jsonArray = new JSONArray();
        for(Twitter Twitter : Twitters){
            jsonArray.add(Twitter.toJson());
        }
        return jsonArray;
    }

    /**
     * get specific "id" Twitter crawl data
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JSONObject getTwitterById(@PathVariable Long id){
        Twitter Twitter = twitterRepository
                                .findById(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return Twitter.toJson();
    }

    /**
     * create specific "id" Twitter crawl data
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public JSONObject createBook(@RequestBody JSONObject TwitterJsonObject) {
        Twitter Twitter = new Twitter().toEntity(TwitterJsonObject);
        Twitter = twitterRepository.save(Twitter);
        return Twitter.toJson();
    }

    /**
     * update specific "id" Twitter crawl data which specific part of data
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public JSONObject updateTwitter(@PathVariable Long id, @RequestBody JSONObject TwitterJsonObject) {
        Twitter Twitter = new Twitter().toEntity(TwitterJsonObject);
        Twitter.setId(id);
        Twitter = twitterRepository.save(Twitter);
        return Twitter.toJson();
    }

    /**
     * update specific "id" Twitter crawl data which entire "id" data
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JSONObject updateTwitterAll(@PathVariable Long id, @RequestBody JSONObject TwitterJsonObject) {
        Twitter Twitter = new Twitter().toEntity(TwitterJsonObject);
        Twitter.setId(id);
        Twitter = twitterRepository.save(Twitter);
        return Twitter.toJson();
    }

    /**
     * delete specific "id" Twitter crawl data
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteBook(@PathVariable Long id) {
        twitterRepository.deleteById(id);
    }



}
