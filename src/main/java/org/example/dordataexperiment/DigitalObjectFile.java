package org.example.dordataexperiment;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table
public record DigitalObjectFile(
        @Id Long id,
        @Column String identifier,
        @Column("file_format") String fileFormat,
        @Column("file_function") String fileFunction,
        @Column Long size,
        @Column String digest,
        @Column LocalDateTime createdAt) {}
