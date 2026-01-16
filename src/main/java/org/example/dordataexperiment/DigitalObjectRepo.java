package org.example.dordataexperiment;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface DigitalObjectRepo extends CrudRepository<DigitalObject, Long>,
        ListPagingAndSortingRepository<DigitalObject, Long> {}
