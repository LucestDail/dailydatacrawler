package com.dailydatahub.dailydatacrawler.crawl.youtube.controller;

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

import com.dailydatahub.dailydatacrawler.crawl.youtube.dao.domain.Youtube;
import com.dailydatahub.dailydatacrawler.crawl.youtube.dao.repository.YoutubeRepository;
import com.dailydatahub.dailydatacrawler.crawl.youtube.service.YoutubeService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/youtube")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class YoutubeController {
    
    @Autowired
    YoutubeService service;

    @Autowired
    private YoutubeRepository repository;

    /**
     * get youtube Search and list of comment
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
     * get youtube Search and list of comment
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
     * get youtube Search and list of comment
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/explore/count", method = RequestMethod.GET)
    @ResponseBody
    public JSONArray exploreCount() throws Exception {
        return service.exploreCount();

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
     * get all list of youtube crawl data
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public JSONArray getAllyoutubes() {
        List<Youtube> youtubes = repository.findAll();
        JSONArray jsonArray = new JSONArray();
        for(Youtube youtube : youtubes){
            jsonArray.add(youtube.toJson());
        }
        return jsonArray;
    }

    /**
     * get specific "id" youtube crawl data
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JSONObject getyoutubeById(@PathVariable Long id){
        Youtube youtube = repository
                                .findById(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return youtube.toJson();
    }

    /**
     * create specific "id" youtube crawl data
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public JSONObject createBook(@RequestBody JSONObject youtubeJsonObject) {
        Youtube youtube = new Youtube().toEntity(youtubeJsonObject);
        youtube = repository.save(youtube);
        return youtube.toJson();
    }

    /**
     * update specific "id" youtube crawl data which specific part of data
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public JSONObject updateyoutube(@PathVariable Long id, @RequestBody JSONObject youtubeJsonObject) {
        Youtube youtube = new Youtube().toEntity(youtubeJsonObject);
        youtube.setId(id);
        youtube = repository.save(youtube);
        return youtube.toJson();
    }

    /**
     * update specific "id" youtube crawl data which entire "id" data
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JSONObject updateyoutubeAll(@PathVariable Long id, @RequestBody JSONObject youtubeJsonObject) {
        Youtube youtube = new Youtube().toEntity(youtubeJsonObject);
        youtube.setId(id);
        youtube = repository.save(youtube);
        return youtube.toJson();
    }

    /**
     * delete specific "id" youtube crawl data
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteBook(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
