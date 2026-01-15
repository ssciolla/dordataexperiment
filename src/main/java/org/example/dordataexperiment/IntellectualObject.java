package org.example.dordataexperiment;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Set;

@Table
public record IntellectualObject(
        @Id Long id,
        @Column("bin_identifier") String binIdentifier,
        @Column("identifier") String identifier,
        @Column("alternate_identifier") String alternateIdentifier,
        @Column String type,
        @Column("version_number") Integer versionNumber,
        @Column("created_at") LocalDateTime createdAt,
        @Column String title,
        @Column String description,
        Set<ObjectFile> objectFiles) {}
