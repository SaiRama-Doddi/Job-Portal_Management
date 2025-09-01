package com.rama.jobportal.job_portal.repository;

import com.rama.jobportal.job_portal.entity.Application;
import com.rama.jobportal.job_portal.entity.ApplicationComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ApplicationCommentRepository extends JpaRepository<ApplicationComment, UUID> {

    // Find all comments for a given job application
    List<ApplicationComment> findByApplication(Application application);
}