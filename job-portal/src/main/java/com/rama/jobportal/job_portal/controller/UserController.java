package com.rama.jobportal.job_portal.controller;

import com.rama.jobportal.job_portal.dto.jobSeekerDTO;
import com.rama.jobportal.job_portal.dto.recruiterDTO;
import com.rama.jobportal.job_portal.dto.userDTO;
import com.rama.jobportal.job_portal.dto.verifyOtpRequestDTO;
import com.rama.jobportal.job_portal.service.userService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class UserController {


    private final userService UserService;


    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody userDTO userDto) {
        try {
            String message = UserService.sendOtpToEmail(userDto.getEmail());
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }


    @PostMapping(value = "/register/jobseeker",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerJobSeeker(
            @RequestPart("user") @Valid userDTO userDto,
            @RequestPart("profile")jobSeekerDTO jobSeekerDto,
            @RequestPart("resume")MultipartFile resume) throws IOException {
        UserService.registerJobSeeker(userDto,jobSeekerDto,resume);
        return ResponseEntity.ok("Job Seeker Regsitered");

    }


    @PostMapping(value = "register/recruiter",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public  ResponseEntity<?> registerRecruiter(@RequestPart("user") userDTO userDto,
                                                @RequestPart("recruiter") recruiterDTO recruiterDto,
                                                @RequestPart("companyLogo") MultipartFile companyLogo)throws IOException{
        UserService.registerRecruiter(userDto,recruiterDto,companyLogo);
        System.out.println("userDto = " + userDto);
        System.out.println("recruiterDto = " + recruiterDto);
        System.out.println("companyLogo = " + companyLogo.getOriginalFilename());

        return ResponseEntity.ok("Recruiter Registered");

    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody verifyOtpRequestDTO dto) {
        try {
            return ResponseEntity.ok(UserService.verifyOtp(dto));
        } catch (Exception e) {
            e.printStackTrace(); // 💥 Print to console
            return ResponseEntity.status(500).body("Verification failed: " + e.getMessage());
        }
    }
    @GetMapping("/hi")
    public String sayHello(){
        return "hi.............rama";
    }


}
