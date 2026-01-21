package org.example.dordataexperiment;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table
public record CurrentIntellectualObject(
        @Id Long id,
        @Column("identifier") String identifier,
        @Column("alternate_identifier") String alternateIdentifier,
        @Column("title") String title,
        @Column String type,
        @Column("created_at") LocalDateTime createdAt,
        @Column("version_number") Integer versionNumber,
        @Column("total_data_size") Long totalDataSize) {}
