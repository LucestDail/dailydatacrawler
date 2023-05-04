package com.dailydatahub.dailydatacrawler.crawl.dcinside.controller;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.dailydatahub.dailydatacrawler.crawl.dcinside.dao.domain.Dcinside;
import com.dailydatahub.dailydatacrawler.crawl.dcinside.dao.repository.DcinsideRepository;
import com.dailydatahub.dailydatacrawler.crawl.dcinside.service.DcinsideService;

import org.springframework.web.bind.annotation.RequestMethod;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/dcinside")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DcinsideController {
    @Autowired
    private final DcinsideService dcinsideService;

    @Autowired
    private DcinsideRepository repository;

    /**
     * get dcinside search keyword data bia 10 page
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/search/{keyword}", method = RequestMethod.GET)
    @ResponseBody
    public JSONArray search(@PathVariable("keyword") String keyword) throws Exception {
        return dcinsideService.search(keyword);
    }

     /**
     * get dcinside explore best contents bia 1 page
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/explore", method = RequestMethod.GET)
    @ResponseBody
    public JSONArray explore() throws Exception {
        return dcinsideService.explore();
    }

    /**
     * get dcinside get all of current explore tab
     * 
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/exploreSave", method = RequestMethod.GET)
    @ResponseBody
    public JSONArray requestExploreSave() throws Exception {
        return dcinsideService.exploreSave();

    }

    /**
     * get all list of dcinside crawl data
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public JSONArray getAlldcinsides() {
        List<Dcinside> Dcinsides = repository.findAll();
        JSONArray jsonArray = new JSONArray();
        for(Dcinside dcinside : Dcinsides){
            jsonArray.add(dcinside.toJson());
        }
        return jsonArray;
    }

    /**
     * get specific "id" dcinside crawl data
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public JSONObject getdcinsideById(@PathVariable Long id){
        Dcinside dcinsides = repository
                                .findById(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return dcinsides.toJson();
    }

    /**
     * create specific "id" dcinside crawl data
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public JSONObject createBook(@RequestBody JSONObject dcinsideJsonObject) {
        Dcinside dcinside = new Dcinside().toEntity(dcinsideJsonObject);
        dcinside = repository.save(dcinside);
        return dcinside.toJson();
    }

    /**
     * update specific "id" dcinside crawl data which specific part of data
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public JSONObject updatedcinside(@PathVariable Long id, @RequestBody JSONObject dcinsideJsonObject) {
        Dcinside dcinside = new Dcinside().toEntity(dcinsideJsonObject);
        dcinside.setId(id);
        dcinside = repository.save(dcinside);
        return dcinside.toJson();
    }

    /**
     * update specific "id" dcinside crawl data which entire "id" data
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public JSONObject updatedcinsideAll(@PathVariable Long id, @RequestBody JSONObject dcinsideJsonObject) {
        Dcinside dcinside = new Dcinside().toEntity(dcinsideJsonObject);
        dcinside.setId(id);
        dcinside = repository.save(dcinside);
        return dcinside.toJson();
    }

    /**
     * delete specific "id" dcinside crawl data
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteBook(@PathVariable Long id) {
        repository.deleteById(id);
    }
}