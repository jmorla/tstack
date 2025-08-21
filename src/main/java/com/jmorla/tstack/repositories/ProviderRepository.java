package com.jmorla.tstack.repositories;

import com.jmorla.tstack.entities.Provider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProviderRepository
        extends CrudRepository<Provider, Long>, PagingAndSortingRepository<Provider, Long> {
}