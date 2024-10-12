package com.evote.services;

import com.evote.dto.CandidateDTO;
import com.evote.models.Candidate;
import com.evote.repositories.CandidateRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateService {
    private final CandidateRepository candidateRepository;

    @Transactional
    public CandidateDTO addCandidate(CandidateDTO candidateDTO) {
        Candidate candidate = new Candidate();
        candidate.setName(candidateDTO.getName());
        candidate.setDescription(candidateDTO.getDescription());
        candidate.setParty(candidateDTO.getParty());
        candidate.setElection(candidateDTO.getElection());
        candidate.setImagePath(candidateDTO.getImagePath());
        candidate.setVoteCount((long)0);

        Candidate savedCandidate = candidateRepository.save(candidate);
        return convertToDTO(savedCandidate);
    }

    public List<CandidateDTO> getAllCandidates() {
        return candidateRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CandidateDTO getCandidate(Long id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found with id: " + id));
        return convertToDTO(candidate);
    }

    @Transactional
    public void deleteCandidate(Long id) {
        if (!candidateRepository.existsById(id)) {
            throw new EntityNotFoundException("Candidate not found with id: " + id);
        }
        candidateRepository.deleteById(id);
    }

    @Transactional
    public CandidateDTO updateCandidate(Long id, CandidateDTO candidateDTO) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found with id: " + id));

        candidate.setName(candidateDTO.getName());
        candidate.setDescription(candidateDTO.getDescription());
        candidate.setParty(candidateDTO.getParty());
        candidate.setElection(candidateDTO.getElection());

        if (candidateDTO.getImagePath() != null) {
            candidate.setImagePath(candidateDTO.getImagePath());
        }

        Candidate updatedCandidate = candidateRepository.save(candidate);
        return convertToDTO(updatedCandidate);
    }

    private CandidateDTO convertToDTO(Candidate candidate) {
        CandidateDTO dto = new CandidateDTO();
        dto.setId(candidate.getId());
        dto.setName(candidate.getName());
        dto.setDescription(candidate.getDescription());
        dto.setVoteCount(candidate.getVoteCount());
        dto.setImagePath(candidate.getImagePath());
        dto.setParty(candidate.getParty());
        dto.setElection(candidate.getElection());
        return dto;
    }
}