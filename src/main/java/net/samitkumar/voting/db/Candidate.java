package net.samitkumar.voting.db;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
public record Candidate(@Id Long id, String name, String partySymbolUrl) {
}
