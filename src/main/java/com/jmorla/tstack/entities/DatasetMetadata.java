package com.jmorla.tstack.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("dataset_metadata")
public class DatasetMetadata {

    @Column("id")
    private Long id;

    @Column("instrument_id")
    private Long instrumentId;

    @Column("total_records")
    private Long totalRecords;

    @Column("start_date")
    private LocalDateTime startDate;

    @Column("end_date")
    private LocalDateTime endDate;

    @Column("dataset_size_bytes")
    private Long datasetSizeBytes;

    @Column("status")
    private String status;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Column("created_at")
    private LocalDateTime createdAt;
}