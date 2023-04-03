package com.dailydatahub.dailydatacrawler.domain.service;

import java.util.List;
import java.util.Optional;

import com.dailydatahub.dailydatacrawler.domain.dao.DomainRaw;
import com.dailydatahub.dailydatacrawler.domain.dto.DomainRawDto;

import jakarta.servlet.http.HttpServletRequest;

public interface DomainService {

    public List<DomainRaw> getDomainRaws(Integer pageNo, Integer pagesize, String sortBy,  Optional<String> domainCode,
            Optional<String> keyword);

    public DomainRaw getDomainRawsDetail(String id);

    public boolean putDomainRaws(DomainRawDto dto, HttpServletRequest request);

    public boolean postDomainRaws(String id, DomainRawDto dto, HttpServletRequest request);

    public boolean deleteDomainRaws(String id);

}
