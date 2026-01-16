package org.example.dordataexperiment;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@SpringBootTest
@Import({TestDatabaseConfig.class})
public class DigitalObjectTests {

    @Autowired
    DigitalObjectRepo digitalObjectRepo;

    @Autowired
    CatalogService catalogService;

    private static final int numOfObjects = 5000;

    Random random = new Random();
    RandomStringUtils randomStringUtils = RandomStringUtils.secure();

    private DigitalObjectFile createFile() {
        var identifier = randomStringUtils.nextAlphanumeric(20);
        var size = random.nextLong(10000000000L);
        var digest = randomStringUtils.nextAlphanumeric(128);
        return new DigitalObjectFile(null, identifier, "application/octet-stream", "content",
                size, digest, LocalDateTime.now());
    }

    private DigitalObjectVersion createVersion(int number) {
        var title = randomStringUtils.nextAlphanumeric(10);
        var description = randomStringUtils.nextAlphanumeric(50);
        var numOfFiles = random.nextInt(10) + 1;
        HashSet<DigitalObjectFile> files = new HashSet<>();
        for (int i = 0; i < numOfFiles; i++) {
            files.add(createFile());
        }
        return new DigitalObjectVersion(null, number, LocalDateTime.now(), title, description, files);
    }

    @BeforeEach
    public void setup() {
        Set<DigitalObject> digObjs = new HashSet<>();
        for (int i = 0; i < numOfObjects; i++) {
            Set<DigitalObjectVersion> versions = new HashSet<>();
            var numOfVersions = random.nextInt(5) + 1;
            for (int j = 0; j < numOfVersions; j++) {
                versions.add(createVersion(j + 1));
            }
            var identifier = UUID.randomUUID().toString();
            var alternateIdentifier = randomStringUtils.nextAlphanumeric(10);
            var digObj = new DigitalObject(
                    null,
                    identifier,
                    identifier,
                    alternateIdentifier,
                    "Curio",
                    LocalDateTime.now(),
                    versions);
            digObjs.add(digObj);
        }
        digitalObjectRepo.saveAll(digObjs);
    }

    @Test
    public void repoFindsAllObjects() {
        Instant start = Instant.now();
        var digObjs = digitalObjectRepo.findAll();
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        System.out.println("digital object - all objects - duration: " + duration.toMillis());
        assertThat(digObjs).hasSize(numOfObjects);

        var numOfVersions = digObjs.stream().map(digObj -> digObj.versions().size())
                .mapToInt(Integer::intValue).sum();
        System.out.println("digital object - all objects - number of versions: " + numOfVersions);

        var numOfFiles = digObjs.stream().map(digObj -> {
            var versions = digObj.versions();
            return versions.stream().map(v -> v.files().size())
                    .mapToInt(Integer::intValue).sum();
        }).mapToInt(Integer::intValue).sum();
        System.out.println("digital object - all objects - number of files: " + numOfFiles);
    }

    @Test
    public void repoFindsOnePageOfObjects() {
        Instant start = Instant.now();
        var digObjsPage = digitalObjectRepo.findAll(PageRequest.of(0, 20));
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        System.out.println("digital object - one page - duration: " + duration.toMillis());
        assertThat(digObjsPage.getContent()).hasSize(20);
    }

    @Test
    public void catalogServiceFindsAllSummaryObjectsUsingDigitalObjects() {
        Instant start = Instant.now();
        var summaryObjects = catalogService.findAllSummaryObjectsUsingDigitalObjects(
                PageRequest.of(0, 20));
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        System.out.println("digital object - object list view - duration: " + duration.toMillis());
        assertThat(summaryObjects).hasSize(20);
    }
}
