package org.example.dordataexperiment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.Collection;

public interface IntellectualObjectRepo extends CrudRepository<IntellectualObject, Long>,
        ListPagingAndSortingRepository<IntellectualObject, Long> {
    Collection<IntellectualObject> findAll();
}
