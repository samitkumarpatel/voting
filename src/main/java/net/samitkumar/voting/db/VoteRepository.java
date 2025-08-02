package net.samitkumar.voting.db;

import org.springframework.data.repository.ListCrudRepository;

public interface VoteRepository extends ListCrudRepository<Vote, Long> {
}
