package com.evote.dto;
import com.evote.dto.CandidateDTO;

import lombok.Data;

@Data
public class CandidateDTO {
    private Long id;
    private String name;
    private String description;
    private Long voteCount;
    private String imagePath;
    private String party;
    private String election;
}