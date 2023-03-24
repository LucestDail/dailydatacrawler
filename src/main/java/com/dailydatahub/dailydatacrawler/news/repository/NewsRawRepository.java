package com.dailydatahub.dailydatacrawler.news.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailydatahub.dailydatacrawler.news.dao.NewsRaw;


public interface NewsRawRepository extends JpaRepository<NewsRaw, Long>{

    List<NewsRaw> findByDomain(String domain);
    
}
