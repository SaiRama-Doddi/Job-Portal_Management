package com.rama.jobportal.job_portal.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ApplyRequest {
    private UUID seekerId;
    private UUID jobId;
    private String notes; // optional
}