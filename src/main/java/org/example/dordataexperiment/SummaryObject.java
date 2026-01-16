package org.example.dordataexperiment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record SummaryObject(
        Long id,
        String identifier,
        String alternateIdentifier,
        String title,
        String type,
        LocalDateTime createdAt) {
    public String getDisplayCreatedAt() {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return createdAt.format(formatter);
    }
}
