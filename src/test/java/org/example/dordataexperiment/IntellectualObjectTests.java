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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
@Import({TestDatabaseConfig.class})
public class IntellectualObjectTests {

    @Autowired
    IntellectualObjectRepo intellectualObjectRepo;

    @Autowired
    CatalogService catalogService;

    private static final int numOfObjects = 5000;

    Random random = new Random();
    RandomStringUtils randomStringUtils = RandomStringUtils.secure();

    int totalNumOfVersions;

    public DigitalObjectVersion createVersion(int number) {
        var title = randomStringUtils.nextAlphanumeric(10);
        var description = randomStringUtils.nextAlphanumeric(50);
        return new DigitalObjectVersion(null, number,
                LocalDateTime.now(), title, description, new HashSet<>());
    }

    private ObjectFile createFile() {
        var identifier = randomStringUtils.nextAlphanumeric(20);
        var size = random.nextLong(10000000000L);
        var digest = randomStringUtils.nextAlphanumeric(128);
        return new ObjectFile(null, identifier, "application/octet-stream", "content",
                size, digest, LocalDateTime.now());
    }

    @BeforeEach
    public void setup() {
        Set<IntellectualObject> intObjs = new HashSet<>();
        for (int i = 0; i < numOfObjects; i++) {
            var identifier = UUID.randomUUID().toString();
            var alternateIdentifier = randomStringUtils.nextAlphanumeric(10);
            var numOfVersions = random.nextInt(5) + 1;
            for (int j = 0; j < numOfVersions; j++) {
                var title = randomStringUtils.nextAlphanumeric(10);
                var description = randomStringUtils.nextAlphanumeric(50);
                var numOfFiles = random.nextInt(10) + 1;
                var files = new HashSet<ObjectFile>();
                var files = Stream.generate(this::createFile).limit(numOfFiles).collect(Collectors.toSet());
                var intObj = new IntellectualObject(
                        null,
                        identifier,
                        identifier,
                        alternateIdentifier,
                        "Curio",
                        j + 1,
                        LocalDateTime.now(),
                        title,
                        description,
                        files);
                intObjs.add(intObj);
            }
        }

        totalNumOfVersions = intObjs.size();
        intellectualObjectRepo.saveAll(intObjs);
    }

    @Test
    public void repoFindsAllObjects() {
        Instant start = Instant.now();
        var intObjs = intellectualObjectRepo.findAll();
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        System.out.println("intellectual object - all objects - duration: " + duration.toMillis());
        assertThat(intObjs).hasSize(totalNumOfVersions);

        var numberOfObjs = intObjs.size();
        System.out.println("intellectual object - all objects - number of objects: " + numberOfObjs);

        var numOfFiles = intObjs.stream().map(intObj -> intObj.objectFiles().size())
                .mapToInt(Integer::intValue).sum();
        System.out.println("intellectual object - all objects - number of files: " + numOfFiles);
    }

    @Test
    public void repoFindsOnePageOfObjects() {
        Instant start = Instant.now();
        var intObjsPage = intellectualObjectRepo.findAll(PageRequest.of(0, 20));
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        System.out.println("intellectual object - one page - " + duration.toMillis());
        assertThat(intObjsPage.getContent()).hasSize(20);
    }

    @Test
    public void catalogServiceFindsSummaryObjectsUsingCurrentIntellectualObjects() {
        Instant start = Instant.now();
        var summaryObjects = catalogService.findAllSummaryObjectsUsingCurrentIntellectualObjects(
                PageRequest.of(0, 20));
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        System.out.println("intellectual object - object list view using current - " + duration.toMillis());
        assertThat(summaryObjects).hasSize(20);
    }
}
