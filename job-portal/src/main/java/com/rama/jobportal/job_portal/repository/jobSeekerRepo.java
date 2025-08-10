package com.rama.jobportal.job_portal.repository;

import com.rama.jobportal.job_portal.entity.JobSeekerDetails;
import com.rama.jobportal.job_portal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface jobSeekerRepo extends JpaRepository<JobSeekerDetails, UUID> {
    Optional<JobSeekerDetails> findByUser(User user);
}
