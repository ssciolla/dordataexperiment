package org.example.dordataexperiment;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import({TestDatabaseConfig.class})
public class CurrentIntellectualObjectTests {

    @Autowired
    CurrentIntellectualObjectRepo currentIntellectualObjectRepo;

    static RandomStringUtils randomStringUtils = RandomStringUtils.secure();

    private static IntellectualObject createKnownIntellectualObject() {
        var files = Set.of(
                new ObjectFile(null, "/some/path", "application/octet-stream",
                        "content", 1000000L, randomStringUtils.nextAlphanumeric(128),
                        LocalDateTime.now()),
                new ObjectFile(null, "/some/other/path", "application/octet-stream",
                        "content", 5000L, randomStringUtils.nextAlphanumeric(128),
                        LocalDateTime.now())
        );
        return new IntellectualObject(null, "abc123", "abc123",
                "my name", "Curio", 1, LocalDateTime.now(),
                "Title", "description", files);
    }

    @BeforeAll
    static void setUp(@Autowired IntellectualObjectRepo intellectualObjectRepo) {
        intellectualObjectRepo.save(createKnownIntellectualObject());
    }

    @Test
    public void currentIntellectualObjectCalculatesTotalDataSize() {
        var currentObj = currentIntellectualObjectRepo.findByIdentifier("abc123").get();
        var totalDataSize = currentObj.totalDataSize();
        assertThat(totalDataSize).isEqualTo(1005000L);
    }
}
