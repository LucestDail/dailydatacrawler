package com.dailydatahub.dailydatacrawler.crawl.twitter.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailydatahub.dailydatacrawler.crawl.twitter.dao.domain.Twitter;

public interface TwitterRepository extends JpaRepository<Twitter, Long>{
    
}
