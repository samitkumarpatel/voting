package net.samitkumar.voting.db;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table
public record Vote(@Id Long id, Long candidateId, String voterId, LocalDate votingTime) {
}
