package com.rama.jobportal.job_portal.repository;

import com.rama.jobportal.job_portal.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface jobRepository extends JpaRepository<Job, UUID> {
}
