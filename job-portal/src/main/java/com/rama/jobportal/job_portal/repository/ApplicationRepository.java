package com.rama.jobportal.job_portal.repository;

import com.rama.jobportal.job_portal.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, UUID>{
    List<Application> findByJobSeeker_SeekerId(UUID seekerId);
    List<Application> findByJob_JobId(UUID jobId);
    Optional<Application> findByJob_JobIdAndJobSeeker_SeekerId(UUID jobId, UUID seekerId);
}