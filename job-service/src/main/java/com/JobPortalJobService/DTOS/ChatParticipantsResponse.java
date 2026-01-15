package com.JobPortalJobService.DTOS;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatParticipantsResponse {

    private Long jobId;
    private Long recruiterId;
    private Long candidateId;

    // getters & setters
}
