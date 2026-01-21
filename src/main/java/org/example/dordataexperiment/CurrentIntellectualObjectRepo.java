package org.example.dordataexperiment;

import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.Optional;

public interface CurrentIntellectualObjectRepo
        extends ListPagingAndSortingRepository<CurrentIntellectualObject, Long> {
    Optional<CurrentIntellectualObject> findByIdentifier(String identifier);
}
