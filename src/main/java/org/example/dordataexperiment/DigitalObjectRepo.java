package org.example.dordataexperiment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.Collection;
import java.util.Optional;

public interface DigitalObjectRepo extends CrudRepository<DigitalObject, Long>,
        ListPagingAndSortingRepository<DigitalObject, Long> {
    Collection<DigitalObject> findAll();
    Optional<DigitalObject> findByIdentifier(String identifier);
}