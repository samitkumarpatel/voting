package net.samitkumar.voting.db;

import org.springframework.data.domain.Limit;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface ErrorVoteRepository extends ListCrudRepository<ErrorVote, Long> {
    Optional<ErrorVote> findErrorVoteByVoterId(String voterId);
}
