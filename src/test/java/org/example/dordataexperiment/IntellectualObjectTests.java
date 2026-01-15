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
public class IntellectualObjectTests {

    @Autowired
    IntellectualObjectRepo intellectualObjectRepo;

    Random random = new Random();
    RandomStringUtils randomStringUtils = RandomStringUtils.secure();

    int totalNumOfVersions;

    public DigitalObjectVersion createVersion(int number) {
        var title = randomStringUtils.nextAlphanumeric(10);
        var description = randomStringUtils.nextAlphanumeric(50);
        return new DigitalObjectVersion(null, number,
                LocalDateTime.now(), title, description, new HashSet<>());
    }

    @BeforeEach
    public void setup() {
        Set<IntellectualObject> intObjs = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            var identifier = UUID.randomUUID().toString();
            var alternateIdentifier = randomStringUtils.nextAlphanumeric(10);
            var numOfVersions = random.nextInt(5) + 1;
            for (int j = 0; j < numOfVersions; j++) {
                var title = randomStringUtils.nextAlphanumeric(10);
                var description = randomStringUtils.nextAlphanumeric(50);
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
                        new HashSet<>());
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
        System.out.println(duration.toMillis());
        assertThat(intObjs).hasSize(totalNumOfVersions);
    }

    @Test
    public void repoFindsOnePageOfObjects() {
        Instant start = Instant.now();
        var intObjsPage = intellectualObjectRepo.findAllBy(PageRequest.of(0, 20));
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        System.out.println(duration.toMillis());
        assertThat(intObjsPage.getContent()).hasSize(20);
    }
}
