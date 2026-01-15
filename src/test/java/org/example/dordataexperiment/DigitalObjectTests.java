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

    Random random = new Random();
    RandomStringUtils randomStringUtils = RandomStringUtils.secure();

    public DigitalObjectVersion createVersion(int number) {
        var title = randomStringUtils.nextAlphanumeric(10);
        var description = randomStringUtils.nextAlphanumeric(50);
        return new DigitalObjectVersion(null, number,
                LocalDateTime.now(), title, description, new HashSet<>());
    }

    @BeforeEach
    public void setup() {
        Set<DigitalObject> digObjs = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
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
        System.out.println(duration.toMillis());
        assertThat(digObjs).hasSize(10000);
    }

    @Test
    public void repoFindsOnePageOfObjects() {
        Instant start = Instant.now();
        var digObjsPage = digitalObjectRepo.findAllBy(PageRequest.of(0, 20));
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        System.out.println(duration.toMillis());
        assertThat(digObjsPage.getContent()).hasSize(20);
    }
}
