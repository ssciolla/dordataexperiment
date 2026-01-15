package org.example.dordataexperiment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface IntellectualObjectRepo extends CrudRepository<IntellectualObject, Long> {
    Page<IntellectualObject> findAllBy(Pageable pageable);
}
