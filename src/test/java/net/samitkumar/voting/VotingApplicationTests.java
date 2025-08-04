package net.samitkumar.voting;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.modulith.core.ApplicationModules;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class VotingApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void applicationModules() {
		ApplicationModules.of(VotingApplication.class)
				.detectViolations()
				.throwIfPresent();
	}

}
