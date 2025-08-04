package net.samitkumar.voting.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table
public record ErrorVote(@Id Long id, LocalDateTime votingTime, Long candidateId, String voterId, String errorMessage, @Transient boolean isSuccess) {
    @PersistenceCreator
    public ErrorVote(Long id, LocalDateTime votingTime, Long candidateId, String voterId, String errorMessage) {
        this(id, votingTime, candidateId, voterId, errorMessage, false);
    }
}
