package org.example.dordataexperiment;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogService {

    private final DigitalObjectRepo digitalObjectRepo;
    private final CurrentIntellectualObjectRepo currentIntellectualObjectRepo;

    public CatalogService(
            DigitalObjectRepo digitalObjectRepo,
            CurrentIntellectualObjectRepo currentIntellectualObjectRepo) {
        this.digitalObjectRepo = digitalObjectRepo;
        this.currentIntellectualObjectRepo = currentIntellectualObjectRepo;
    }

    public List<SummaryObject> findAllSummaryObjectsUsingDigitalObjects(Pageable pageable) {
        var page = digitalObjectRepo.findAll(pageable);
        var digObjs = page.getContent();
        return digObjs.stream().map(digObj -> {
            var latestVersion = digObj.getLatestVersion();
            return new SummaryObject(digObj.id(), digObj.identifier(),
                    digObj.alternateIdentifier(), latestVersion.title(),
                    digObj.type(), digObj.createdAt(), latestVersion.getTotalDataSize());
        }).toList();
    }

    public List<SummaryObject> findAllSummaryObjectsUsingCurrentIntellectualObjects(Pageable pageable) {
        var page = currentIntellectualObjectRepo.findAll(pageable);
        var currentObjs = page.getContent();
        return currentObjs.stream().map(currentObj ->
            new SummaryObject(currentObj.id(), currentObj.identifier(),
                    currentObj.alternateIdentifier(), currentObj.title(), currentObj.type(),
                    currentObj.createdAt(), currentObj.totalDataSize())
        ).toList();
    }
}
