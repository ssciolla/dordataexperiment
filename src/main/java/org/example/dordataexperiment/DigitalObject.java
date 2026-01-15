package org.example.dordataexperiment;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;
import java.util.Set;

public record DigitalObject (
        @Id Long id,
        @Column("bin_identifier") String binIdentifier,
        @Column("identifier") String identifier,
        @Column("alternate_identifier") String alternateIdentifier,
        @Column String type,
        @Column("created_at") LocalDateTime createdAt,
        Set<DigitalObjectVersion> versions) {}
