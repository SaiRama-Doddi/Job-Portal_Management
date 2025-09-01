package com.rama.jobportal.job_portal.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "application_comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationComment {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Application application;

    @Column(nullable = false, length = 16)
    private String authorType; // "JOB_SEEKER" or "RECRUITER"

    @Column(nullable = false, length = 2000)
    private String comment;

    private String commentedBy; // Recruiter/JobSeeker/Admin etc.

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}

