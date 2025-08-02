-- Create candidates table
CREATE TABLE candidate (
       id BIGSERIAL PRIMARY KEY,
       name VARCHAR(255) NOT NULL,
       party_symbol_url VARCHAR(500)
);

-- Create vote table
CREATE TABLE vote (
      id BIGSERIAL PRIMARY KEY,
      candidate_id BIGINT NOT NULL,
      voter_id VARCHAR(255) NOT NULL,
      CONSTRAINT fk_vote_candidate FOREIGN KEY (candidate_id) REFERENCES candidate(id)
);

CREATE TABLE error_vote (
      id BIGSERIAL PRIMARY KEY,
      vote_id BIGINT,
      candidate_id BIGINT,
      voter_id VARCHAR(255),
      error_message TEXT NOT NULL
);

-- Create indexes for better performance
CREATE INDEX idx_vote_candidate_id ON vote(candidate_id);
CREATE INDEX idx_vote_voter_id ON vote(voter_id);

INSERT INTO candidate (name, party_symbol_url) VALUES
('Alice Smith', 'https://example.com/symbols/alice.png'),
('Bob Johnson', 'https://example.com/symbols/bob.png'),
('Charlie Brown', 'https://example.com/symbols/charlie.png');