package com.jmorla.tstack.repositories;

import com.jmorla.tstack.entities.Instrument;
import com.jmorla.tstack.models.InstrumentWithProvider;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface InstrumentRepository extends CrudRepository<Instrument, Long>,
        PagingAndSortingRepository<Instrument, Long> {


    @Query("""
            SELECT i.id,
                   i.provider_id,
                   i.platform_id,
                   i.symbol,
                   i.name,
                   i.description,
                   i.created_at,
                   i.updated_at,
                   p.name AS provider_name,
                   p.description AS provider_description,
                   p.created_at AS provider_created_at,
                   p.updated_at AS provider_updated_at,
                   dm.id AS dataset_id,
                   dm.total_records,
                   dm.start_date,
                   dm.end_date,
                   dm.dataset_size_bytes,
                   dm.status,
                   dm.created_at AS dataset_created_at,
                   dm.updated_at AS dataset_updated_at
            FROM instruments i
            LEFT JOIN providers p ON i.provider_id = p.id
            LEFT JOIN dataset_metadata dm ON i.id = dm.instrument_id
            ORDER BY i.id
            LIMIT :limit OFFSET :offset
            """)
    List<InstrumentWithProvider> findAllWithProvider(@Param("limit") int limit, @Param("offset") int offset);

    @Query("""
            SELECT COUNT(i.id)
            FROM instruments i
            LEFT JOIN providers p ON i.provider_id = p.id
            LEFT JOIN dataset_metadata dm ON i.id = dm.instrument_id
            """)
    long countAllWithProvider();

    @Query("""
            SELECT i.id,
                   i.provider_id,
                   i.platform_id,
                   i.symbol,
                   i.name,
                   i.description,
                   i.created_at,
                   i.updated_at,
                   p.name AS provider_name,
                   p.description AS provider_description,
                   p.created_at AS provider_created_at,
                   p.updated_at AS provider_updated_at,
                   dm.id AS dataset_id,
                   dm.total_records,
                   dm.start_date,
                   dm.end_date,
                   dm.dataset_size_bytes,
                   dm.status,
                   dm.created_at AS dataset_created_at,
                   dm.updated_at AS dataset_updated_at
            FROM instruments i
            LEFT JOIN providers p ON i.provider_id = p.id
            LEFT JOIN dataset_metadata dm ON i.id = dm.instrument_id
            WHERE (:instrumentFilter IS NULL OR LOWER(i.name) LIKE LOWER(:instrumentFilter) OR LOWER(i.symbol) LIKE LOWER(:instrumentFilter))
              AND (:status IS NULL OR LOWER(dm.status) = LOWER(:status))
              AND (:providerFilter IS NULL OR LOWER(p.name) LIKE LOWER(:providerFilter))
            ORDER BY i.id
            LIMIT :limit OFFSET :offset
            """)
    List<InstrumentWithProvider> searchInstrumentsWithProvider(
            @Param("instrumentFilter") String instrumentFilter,
            @Param("status") String status,
            @Param("providerFilter") String providerFilter,
            @Param("limit") int limit,
            @Param("offset") int offset);

    @Query("""
            SELECT COUNT(i.id)
            FROM instruments i
            LEFT JOIN providers p ON i.provider_id = p.id
            LEFT JOIN dataset_metadata dm ON i.id = dm.instrument_id
            WHERE (:instrumentFilter IS NULL OR LOWER(i.name) LIKE LOWER(:instrumentFilter) OR LOWER(i.symbol) LIKE LOWER(:instrumentFilter))
              AND (:status IS NULL OR LOWER(dm.status) = LOWER(:status))
              AND (:providerFilter IS NULL OR LOWER(p.name) LIKE LOWER(:providerFilter))
            """)
    long countSearchInstrumentsWithProvider(
            @Param("instrumentFilter") String instrumentFilter,
            @Param("status") String status,
            @Param("providerFilter") String providerFilter);

}
