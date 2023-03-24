package com.dailydatahub.dailydatacrawler.news.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dailydatahub.dailydatacrawler.news.dao.NewsRaw;
import com.dailydatahub.dailydatacrawler.news.dto.NewsRawDto;
import com.dailydatahub.dailydatacrawler.news.repository.NewsRawRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class NewsServiceImpl implements NewsService{

    private NewsRawRepository repository;

    @Override
    public List<NewsRaw> getNewsRaws(String domain){
        return repository.findByDomain(domain);
    }

    public List<NewsRaw> getNewsRaws(String domain, String keyword){
        return repository.findByDomain(domain);
    }

    @Override
    public NewsRaw getNewsRawsDetail(String id) {
        Optional<NewsRaw> news = repository.findById(Long.parseLong(id));
        return news.isPresent() ? news.get() : null;
    }

    @Override
    public boolean putNewsRaws(NewsRawDto dto, HttpServletRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'putNewsRaws'");
    }

    @Override
    public boolean postNewsRaws(String id, NewsRawDto dto, HttpServletRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'postNewsRaws'");
    }

    @Override
    public boolean deleteNewsRaws(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteNewsRaws'");
    }

    private Page<NewsRaw> findPaginated(Pageable pageable, String news, Optional<String> keyword) {
		List<NewsRaw> newsRawsList = new ArrayList<>();
		if (keyword.isPresent()) {
			newsRawsList = getNewsRaws(news, keyword.get());
		} else {
			newsRawsList = getNewsRaws(news);
		}
		Collections.reverse(newsRawsList);
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
		int startItem = currentPage * pageSize;


		List<NewsRaw> list;
		if (newsRawsList.size() < startItem) {
			list = Collections.emptyList();
		} else {
			int toIndex = Math.min(startItem + pageSize, newsRawsList.size());
			list = newsRawsList.subList(startItem, toIndex);
		}

		Page<NewsRaw> boardPage = new PageImpl<NewsRaw>(list, PageRequest.of(currentPage, pageSize), newsRawsList.size());

		return boardPage;
	}
    
}
