package com.rama.jobportal.job_portal.dto;


import com.rama.jobportal.job_portal.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class userDTO {

    @Email
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private Role role;


    private  String otp;

    
    private boolean verified= false;

    private LocalDateTime localDateTime;
}
