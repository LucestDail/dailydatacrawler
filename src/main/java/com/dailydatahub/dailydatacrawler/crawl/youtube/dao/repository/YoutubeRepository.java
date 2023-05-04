package com.dailydatahub.dailydatacrawler.crawl.youtube.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailydatahub.dailydatacrawler.crawl.youtube.dao.domain.Youtube;

public interface YoutubeRepository extends JpaRepository<Youtube, Long>{
    
}
