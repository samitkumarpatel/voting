package net.samitkumar.voting.db;

import org.springframework.data.repository.ListCrudRepository;

public interface CandidateRepository extends ListCrudRepository<Candidate, Long> {
}
