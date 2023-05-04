package com.dailydatahub.dailydatacrawler.crawl.dcinside.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dailydatahub.dailydatacrawler.crawl.dcinside.dao.domain.Dcinside;

@Repository
public interface DcinsideRepository extends JpaRepository<Dcinside, Long>{
    
}
