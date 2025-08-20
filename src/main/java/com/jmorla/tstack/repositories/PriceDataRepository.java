package com.jmorla.tstack.repositories;

import com.jmorla.tstack.entities.PriceData;
import org.springframework.data.repository.CrudRepository;

public interface PriceDataRepository extends CrudRepository<PriceData, Long> {
}
