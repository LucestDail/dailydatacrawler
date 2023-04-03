package com.dailydatahub.dailydatacrawler.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dailydatahub.dailydatacrawler.domain.dao.DomainRaw;
import com.dailydatahub.dailydatacrawler.domain.dto.DomainRawDto;
import com.dailydatahub.dailydatacrawler.domain.repository.DomainRawRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class DomainServiceImpl implements DomainService {

    private DomainRawRepository repository;

    public List<DomainRaw> getDomainRaws(Integer pageNo, Integer pageSize, String sortBy, Optional<String> domainCode,
            Optional<String> keyword) {
        Specification<DomainRaw> spec = Specification.where(null);
        if (domainCode.isPresent())
        spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("domainCode"), domainCode));
        if (keyword.isPresent())
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("keyword"), keyword));
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(Order.desc(sortBy)));
        Page<DomainRaw> pagedResult = repository.findAll(spec, paging);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<DomainRaw>();
        }
    }

    @Override
    public DomainRaw getDomainRawsDetail(String id) {
        Optional<DomainRaw> news = repository.findById(Long.parseLong(id));
        return news.isPresent() ? news.get() : null;
    }

    @Override
    public boolean putDomainRaws(DomainRawDto dto, HttpServletRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'putDomainRaws'");
    }

    @Override
    public boolean postDomainRaws(String id, DomainRawDto dto, HttpServletRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'postDomainRaws'");
    }

    @Override
    public boolean deleteDomainRaws(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteDomainRaws'");
    }

    private Page<DomainRaw> findPaginated(Pageable pageable, String domainCode, Optional<String> keyword) {
        return repository.findAll(pageable);
    }

}
