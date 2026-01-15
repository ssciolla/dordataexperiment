package org.example.dordataexperiment;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;
import java.util.Set;

public record DigitalObjectVersion(
        @Id Long id,
        @Column("version_number") Integer versionNumber,
        @Column("created_at") LocalDateTime createdAt,
        @Column String title,
        @Column String description,
        Set<DigitalObjectFile> files) {}
