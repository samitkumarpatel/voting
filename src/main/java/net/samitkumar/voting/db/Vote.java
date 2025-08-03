package net.samitkumar.voting.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table
public record Vote(@Id Long id, Long candidateId, String voterId, @DateTimeFormat(pattern="yyyy-MM-dd") LocalDateTime votingTime) {
}
