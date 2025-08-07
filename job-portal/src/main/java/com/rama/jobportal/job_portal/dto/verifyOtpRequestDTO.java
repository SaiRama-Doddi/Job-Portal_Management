package com.rama.jobportal.job_portal.dto;


import lombok.Data;

@Data
public class verifyOtpRequestDTO {

    private String email;
    private String otp;

}
