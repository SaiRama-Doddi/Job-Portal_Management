package com.rama.jobportal.job_portal.repository;

import com.rama.jobportal.job_portal.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface jobRepository extends JpaRepository<Job, UUID> {






    @Query("SELECT j FROM Job j WHERE " +
            "(" +
            "(:skill1 IS NULL OR LOWER(j.skills) LIKE %:skill1%) OR " +
            "(:skill2 IS NULL OR LOWER(j.skills) LIKE %:skill2%) OR " +
            "(:skill3 IS NULL OR LOWER(j.skills) LIKE %:skill3%)" +
            ") " +
            "AND (:location IS NULL OR LOWER(j.location) LIKE %:location%) " +
            "AND (:experience IS NULL OR LOWER(j.experienceNeeded) LIKE %:experience%)")
    List<Job> findRecommendedJobs(
            @Param("skill1") String skill1,
            @Param("skill2") String skill2,
            @Param("skill3") String skill3,
            @Param("location") String location,
            @Param("experience") String experience
    );


}
