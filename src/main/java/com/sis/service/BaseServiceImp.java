package com.sis.service;

import com.sis.dto.college.GeneralSearchRequest;
import com.sis.entity.BaseEntity;
import com.sis.exception.ItemNotFoundException;
import com.sis.util.PageQueryUtil;
import com.sis.util.PageResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public abstract class BaseServiceImp<E extends BaseEntity> implements BaseService<E> {

    @Override
    public List<E> findAll() {
        List<E> entity = Repository().findAll();
        return entity;
    }

    @Override
    public E findById(Long id) {

        return Repository().findById(id).orElseThrow(() -> new ItemNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = false)
    public E save(E entity) {
        return Repository().save(entity);
    }

    @Override
    @Transactional(readOnly = false)
    public List<E> saveAll(List<E> entity) {
        return Repository().saveAll(entity);
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(E entity) {
        Repository().delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        Repository().deleteById(id);
    }

    @Override
    public PageResult<E> getDataPage(PageQueryUtil pageUtil) {
        Pageable pageable = PageRequest.of(pageUtil.getPage() -1, pageUtil.getLimit());
        Page<E> page = this.Repository().findAll(pageable);
        PageResult<E> pageResult = new PageResult<E>(page.getContent(), (int) page.getTotalElements(),
                pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public PageResult<E> getDataPage(PageQueryUtil pageUtil, @Nullable String sortField, @Nullable Direction sortDirection) {
        Sort sort = sortDirection.equals(Direction.ASC) ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageUtil.getPage() - 1, pageUtil.getLimit(), sort);
        Page<E> page = this.Repository().findAll(pageable);
        PageResult<E> pageResult = new PageResult<E>(page.getContent(), (int) page.getTotalElements(),
                pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    public Sort constructSortObject(GeneralSearchRequest generalSearchRequest, String defaultSortField) {
        if (generalSearchRequest.getSortDirection() == null) {
            return Sort.by(Sort.Direction.ASC, defaultSortField);
        }
        return Sort.by(Sort.Direction.valueOf(generalSearchRequest.getSortDirection()), generalSearchRequest.getSortBy());
    }

}
