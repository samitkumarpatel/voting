package net.samitkumar.voting.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
public record ErrorVote(@Id Long id, Long voteId, Long candidateId, String voterId, String errorMessage) {
}
