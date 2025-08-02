package net.samitkumar.voting.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
public record Vote(@Id Long id, Long candidateId, String voterId) {
}
