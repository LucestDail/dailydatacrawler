package com.dailydatahub.dailydatacrawler.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dailydatahub.dailydatacrawler.domain.dao.DomainRaw;
import com.dailydatahub.dailydatacrawler.domain.repository.DomainRawRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class DomainServiceImpl implements DomainService {

    private DomainRawRepository repository;

    @Override
    public List<DomainRaw> getDomainRaws(Integer pageNo, Integer pageSize, String sortBy, Optional<String> domainCode,
            Optional<String> keyword) {
        Specification<DomainRaw> spec = Specification.where(null);
        if (domainCode.isPresent())
            spec = spec
                    .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("domainCode"), domainCode));
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
        Optional<DomainRaw> damainRaw = repository.findById(Long.parseLong(id));
        return damainRaw.isPresent() ? damainRaw.get() : null;
    }

    @Override
    public boolean putDomainRaws(DomainRaw domainRaw, HttpServletRequest request) {
        return Objects.isNull(repository.save(domainRaw.toEntity())) ? false : true;
    }

    @Override
    public boolean postDomainRaws(String id, DomainRaw domainRaw, HttpServletRequest request) {
        Optional<DomainRaw> currentDomainRaw = repository.findById(Long.parseLong(id));
        return currentDomainRaw.isPresent()
                ? Objects.isNull(repository.saveAndFlush(currentDomainRaw.get().toEntity(domainRaw)))
                    ? false
                    : true
                : false;
    }

    @Override
    public boolean deleteDomainRaws(String id) {
        repository.deleteById(Long.parseLong(id));
        repository.flush();
        return repository.findById(Long.parseLong(id)).isPresent() ? false : true;

    }

}
