package com.rama.jobportal.job_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationDTO {
    private UUID id;
    private String jobTitle;
    private String seekerName;
    private String status;
    private String seekerGender;
    private String seekerEmail;
    // don’t include byte[] resume here

    public ApplicationDTO(UUID id, String jobTitle, String seekerEmail, String status) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.seekerEmail = seekerEmail;
        this.status = status;
    }
}
