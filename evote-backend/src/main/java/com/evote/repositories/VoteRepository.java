// VoteRepository.java
package com.evote.repositories;

import com.evote.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByUserIdAndCandidateId(Long userId, Long candidateId);
}