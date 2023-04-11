package com.dailydatahub.dailydatacrawler.domain.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dailydatahub.dailydatacrawler.domain.dao.DomainRaw;
import com.dailydatahub.dailydatacrawler.domain.service.DomainService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/domain")
public class DomainController {

    @Autowired
    DomainService service;

    /**
     * select specific domain list
     * 
     * @param news select news category
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<List<DomainRaw>> getDomainRaws(@RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam("domainCode") Optional<String> domainCode,
            @RequestParam("keyword") Optional<String> keyword) {
        return new ResponseEntity<List<DomainRaw>>(service.getDomainRaws(pageNo, pageSize, sortBy, domainCode, keyword),
                new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * select detail
     * 
     * @param news select news
     * @param id   select id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public DomainRaw getDomainRawsList(@PathVariable("id") String id) {
        return service.getDomainRawsDetail(id);
    }

    /**
     * insert 'domainRaw'
     * where 'request'
     * 
     * @param domainRaw
     * @param request
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public boolean putDomainRaws(@RequestBody DomainRaw domainRaw, HttpServletRequest request) {
        return service.putDomainRaws(domainRaw, request);
    }

    /**
     * update
     * where 'id'
     * and 'request'
     * set 'domainRaw'
     * 
     * @param domainRaw
     * @param request
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public boolean postDomainRaws(@RequestBody DomainRaw domainRaw, HttpServletRequest request,
            @PathVariable("id") String id) {
        return service.postDomainRaws(id, domainRaw, request);
    }

    /**
     * delete
     * where 'id'
     * 
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public boolean deleteDomainRaws(@PathVariable("id") String id) {
        return service.deleteDomainRaws(id);
    }

}
