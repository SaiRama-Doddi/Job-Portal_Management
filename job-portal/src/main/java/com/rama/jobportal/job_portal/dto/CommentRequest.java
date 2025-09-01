package com.rama.jobportal.job_portal.dto;

import lombok.Data;

@Data
public class CommentRequest {
    private String authorType; // JOB_SEEKER or RECRUITER
    private String comment;
}