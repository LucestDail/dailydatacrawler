package com.dailydatahub.dailydatacrawler.domain.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dailydatahub.dailydatacrawler.domain.dao.DomainRaw;


public interface DomainRawRepository extends JpaRepository<DomainRaw, Long>, JpaSpecificationExecutor<DomainRaw> {

    List<DomainRaw> findByDomainCode(String domainCode);
    
}
