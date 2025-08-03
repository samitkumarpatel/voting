package net.samitkumar.voting.db;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface VoteRepository extends ListCrudRepository<Vote, Long> {
    /*@Query("""
        SELECT c.id AS candidate_id, COUNT(v.candidate_id) AS count
        FROM vote v
        JOIN candidate c ON v.candidate_id = c.id
        GROUP BY c.id
    """)*/
    @Query("""
    SELECT c.id AS candidate_id, COUNT(v.candidate_id) AS count
    FROM candidate c
    LEFT JOIN vote v ON c.id = v.candidate_id
    GROUP BY c.id
    ORDER BY c.id
    """)
    List<VoteCount> votingResults();
}
