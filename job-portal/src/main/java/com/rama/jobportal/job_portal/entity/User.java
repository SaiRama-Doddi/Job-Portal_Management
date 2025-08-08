package com.rama.jobportal.job_portal.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {


    @Id
    @GeneratedValue
    private UUID userId;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private  String otp;

    @Column(name = "verified")
    private boolean verified= false;

    @Column(name = "local_date_time")
    private LocalDateTime localDateTime;

    //@OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
   // private  JobSeekerDetails jobSeekerDetails;

    //@OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    //private  RecruiterProfileDetails recruiterProfileDetails;

}
