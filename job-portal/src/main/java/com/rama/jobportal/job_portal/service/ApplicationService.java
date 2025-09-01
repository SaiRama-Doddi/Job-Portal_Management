package com.rama.jobportal.job_portal.service;



import com.rama.jobportal.job_portal.dto.ApplyRequest;
import com.rama.jobportal.job_portal.dto.UpdateStatusRequest;
import com.rama.jobportal.job_portal.entity.*;

import com.rama.jobportal.job_portal.repository.ApplicationCommentRepository;
import com.rama.jobportal.job_portal.repository.ApplicationRepository;
import com.rama.jobportal.job_portal.repository.jobRepository;
import com.rama.jobportal.job_portal.repository.jobSeekerRepo;
import com.rama.jobportal.job_portal.utils.ResumeParser;
import com.rama.jobportal.job_portal.utils.SkillUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final jobRepository jobRepository;
    private final jobSeekerRepo jobSeekerRepository; // your existing repo
    private final ApplicationCommentRepository commentRepository; // if using comments


    // Configure your threshold here: at least N skills must match resume text
    private static final int MIN_MATCH_COUNT = 1;

    @Transactional
    public Application apply(ApplyRequest req) {
        Job job = jobRepository.findById(req.getJobId())
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        JobSeekerDetails seeker = jobSeekerRepository.findById(req.getSeekerId())
                .orElseThrow(() -> new IllegalArgumentException("Job seeker not found"));

        if (seeker.getResume() == null || seeker.getResume().length == 0) {
            throw new IllegalArgumentException("Resume is required to apply.");
        }

        // Parse resume text
        final String resumeText;
        try {
            resumeText = ResumeParser.extractText(seeker.getResume());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read resume. Please re-upload a valid PDF/DOC/DOCX.", e);
        }

        // Normalize job skills (comma-separated)
        var jobSkillSet = SkillUtils.normalizeCsvSkills(job.getSkills());

        // Count matches
        long matchCount = SkillUtils.countMatchesInText(jobSkillSet, resumeText);

        if (matchCount < MIN_MATCH_COUNT) {
            throw new IllegalArgumentException("Application blocked: your resume does not mention required skills.");
        }

        // Prevent duplicate application
        applicationRepository.findByJob_JobIdAndJobSeeker_SeekerId(job.getJobId(), seeker.getSeekerId())
                .ifPresent(a -> { throw new IllegalStateException("You already applied to this job."); });

        Application entity = Application.builder()
                .job(job)
                .jobSeeker(seeker)
                .status(ApplicationStatus.APPLIED)
                .notes(req.getNotes())
                .build();

        return applicationRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<Application> listForSeeker(UUID seekerId) {
        return applicationRepository.findByJobSeeker_SeekerId(seekerId);
    }

    @Transactional(readOnly = true)
    public List<Application> listForJob(UUID jobId) {
        return applicationRepository.findByJob_JobId(jobId);
    }

    @Transactional
    public Application updateStatus(UUID applicationId, UpdateStatusRequest req/*, User currentUser */) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        // TODO: authorize recruiter / admin only (based on your auth)
        ApplicationStatus newStatus = ApplicationStatus.valueOf(req.getStatus().toUpperCase(Locale.ROOT));

        // Optional: validate allowed transitions if you want
        app.setStatus(newStatus);
        return applicationRepository.save(app);
    }

    @Transactional
    public ApplicationComment addComment(UUID applicationId, String authorType, String comment) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));
        // TODO: authorization (only applicant or the job's recruiter)

        ApplicationComment c = ApplicationComment.builder()
                .application(app)
                .authorType(authorType)
                .comment(comment)
                .build();

        return commentRepository.save(c);
    }
}
