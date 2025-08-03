package net.samitkumar.voting.db;

import net.samitkumar.voting.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
public class DbIntegrationTests {

    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private VoteRepository voteRepository;

    @Test
    void dbIntegrationTest() {
        assertAll(
                () -> candidateRepository
                        .saveAll(
                                List.of(
                                        new Candidate(null, "John Doe", "http://example.com/symbol1.png"),
                                        new Candidate(null, "Jane Smith", "http://example.com/symbol2.png")
                                )
                        ),
                () -> candidateRepository
                        .findAll()
                        .forEach(System.out::println),
                () -> {
                    var candidates = candidateRepository.findAll();
                    voteRepository
                            .saveAll(
                                    List.of(
                                            new Vote(null, candidates.getFirst().id(), "voter1", LocalDate.now()),
                                            new Vote(null, candidates.getFirst().id(), "voter2", LocalDate.now()),
                                            new Vote(null, candidates.getFirst().id(), "voter3", LocalDate.now()),
                                            new Vote(null, candidates.getLast().id(), "voter4", LocalDate.now())
                                    )
                            );
                },
                () -> voteRepository
                        .findAll()
                        .forEach(System.out::println),
                () -> voteRepository
                        .votingResults()
                        .forEach(System.out::println)

        );
    }
}
