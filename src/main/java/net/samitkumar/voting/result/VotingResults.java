package net.samitkumar.voting.result;

import net.samitkumar.voting.db.VoteCount;

import java.time.LocalDateTime;
import java.util.List;

public record VotingResults(LocalDateTime timestamp, List<VoteCount> voteCount) { }
