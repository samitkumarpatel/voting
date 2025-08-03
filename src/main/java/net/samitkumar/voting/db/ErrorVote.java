package net.samitkumar.voting.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table
public record ErrorVote(@Id Long id, LocalDateTime votingTime, Long candidateId, String voterId, String errorMessage) { }
