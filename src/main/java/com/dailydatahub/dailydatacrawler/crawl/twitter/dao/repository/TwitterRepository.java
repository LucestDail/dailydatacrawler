package com.dailydatahub.dailydatacrawler.crawl.twitter.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dailydatahub.dailydatacrawler.crawl.twitter.dao.domain.Twitter;

@Repository
public interface TwitterRepository extends JpaRepository<Twitter, Long>{
    
}
