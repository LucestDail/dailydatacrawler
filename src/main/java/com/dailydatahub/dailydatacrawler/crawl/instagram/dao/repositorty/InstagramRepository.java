package com.dailydatahub.dailydatacrawler.crawl.instagram.dao.repositorty;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dailydatahub.dailydatacrawler.crawl.instagram.domain.Instagram;

@Repository
public interface InstagramRepository extends JpaRepository<Instagram, Long>{
    
}
