package net.samitkumar.voting;

import net.samitkumar.voting.db.Candidate;
import net.samitkumar.voting.db.CandidateRepository;
import net.samitkumar.voting.db.VoteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class VotingApplicationTests {

	@Test
	void contextLoads() {
	}

}
