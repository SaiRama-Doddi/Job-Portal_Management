package com.rama.jobportal.job_portal.repository;

import com.rama.jobportal.job_portal.entity.RecruiterProfileDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface recruiterProfileRepo extends JpaRepository<RecruiterProfileDetails, UUID> {
}
