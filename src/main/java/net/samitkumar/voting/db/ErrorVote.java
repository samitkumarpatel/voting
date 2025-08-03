package net.samitkumar.voting.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table
public record ErrorVote(@Id Long id, LocalDate votingTime, Long candidateId, String voterId, String errorMessage) { }
