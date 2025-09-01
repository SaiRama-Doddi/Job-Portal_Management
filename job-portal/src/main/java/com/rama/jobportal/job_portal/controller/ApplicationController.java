package com.rama.jobportal.job_portal.controller;



import com.rama.jobportal.job_portal.dto.ApplicationDTO;
import com.rama.jobportal.job_portal.dto.ApplyRequest;
import com.rama.jobportal.job_portal.dto.CommentRequest;
import com.rama.jobportal.job_portal.dto.UpdateStatusRequest;
import com.rama.jobportal.job_portal.entity.Application;
import com.rama.jobportal.job_portal.entity.ApplicationComment;
import com.rama.jobportal.job_portal.repository.ApplicationRepository;
import com.rama.jobportal.job_portal.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final ApplicationRepository applicationRepository;

    @PostMapping("/apply")
    public ResponseEntity<ApplicationDTO> apply(@RequestBody ApplyRequest req) {
        Application saved = applicationService.apply(req);
        ApplicationDTO dto = new ApplicationDTO(
                saved.getId(),
                saved.getJob().getJobTitle(),
                saved.getJobSeeker().getUser().getEmail(),
                saved.getStatus().name()
        );
        return ResponseEntity.status(201).body(dto);
    }


    @GetMapping("/seeker/{seekerId}")
    public List<ApplicationDTO> getApplicationsBySeeker(@PathVariable UUID seekerId) {
        return applicationRepository.findByJobSeeker_SeekerId(seekerId)
                .stream()
                .map(app -> new ApplicationDTO(
                        app.getId(),
                        app.getJob().getJobTitle(),
                        app.getJobSeeker().getName(),   // ✅ use name
                        app.getJobSeeker().getGender(),
                        app.getJobSeeker().getUser().getEmail(),  // ✅ added missing comma
                        app.getStatus().name()
                ))
                .collect(Collectors.toList());
    }







    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<ApplicationDTO>> listForJob(@PathVariable UUID jobId) {
        List<ApplicationDTO> dtos = applicationService.listForJob(jobId)
                .stream()
                .map(app -> new ApplicationDTO(
                        app.getId(),
                        app.getJob().getJobTitle(),
                        app.getJobSeeker().getUser().getEmail(),
                        app.getStatus().name()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }


    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<Application> updateStatus(@PathVariable UUID applicationId,
                                                    @RequestBody UpdateStatusRequest req) {
        Application updated = applicationService.updateStatus(applicationId, req);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{applicationId}/comments")
    public ResponseEntity<ApplicationComment> comment(@PathVariable UUID applicationId,
                                                      @RequestBody CommentRequest req) {
        ApplicationComment c = applicationService.addComment(applicationId, req.getAuthorType(), req.getComment());
        return ResponseEntity.status(201).body(c);
    }




}

