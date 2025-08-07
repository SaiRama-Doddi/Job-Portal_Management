package com.rama.jobportal.job_portal.service;

import com.rama.jobportal.job_portal.dto.jobSeekerDTO;
import com.rama.jobportal.job_portal.dto.recruiterDTO;
import com.rama.jobportal.job_portal.dto.userDTO;
import com.rama.jobportal.job_portal.dto.verifyOtpRequestDTO;
import com.rama.jobportal.job_portal.entity.JobSeekerDetails;
import com.rama.jobportal.job_portal.entity.RecruiterProfileDetails;
import com.rama.jobportal.job_portal.entity.Role;
import com.rama.jobportal.job_portal.entity.User;
import com.rama.jobportal.job_portal.repository.jobSeekerRepo;
import com.rama.jobportal.job_portal.repository.recruiterProfileRepo;
import com.rama.jobportal.job_portal.repository.userRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class userService {


    private final userRepository userRepo;
    private final jobSeekerRepo jobseekerRepo;
    private final recruiterProfileRepo recruiterprofileRepo;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;


    public void registerJobSeeker(userDTO userDto, jobSeekerDTO jobSeekerDto, MultipartFile resume) throws IOException {
        Optional<User> optionalUser = userRepo.findByEmail(userDto.getEmail());
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("Please verify your email before registering.");
        }

        User user = optionalUser.get();

        if (!user.isVerified()) {
            throw new IllegalArgumentException("Email not verified. Please verify OTP before registering.");
        }

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(Role.JOB_SEEKER);
        user.setOtp(null);
        // Set password only now

        String skill = String.join(",", jobSeekerDto.getSkills());

        JobSeekerDetails profile = JobSeekerDetails.builder()
                .user(user)
                .experienceYears(jobSeekerDto.getExperienceYears())
                .currentLocation(jobSeekerDto.getCurrentLocation())
                .preferredLocation(jobSeekerDto.getPreferredLocation())
                .skills(skill)
                .resume(resume.getBytes())
                .build();

        user.setJobSeekerDetails(profile);

        userRepo.save(user);
    }



    public String sendOtpToEmail(String email) {
        Optional<User> optionalUser = userRepo.findByEmail(email);
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);
        if (optionalUser.isPresent()) {
            //throw new IllegalArgumentException("Email already exists");
            User exisitingUser=optionalUser.get();
            exisitingUser.setOtp(otp);
            exisitingUser.setVerified(false);
            exisitingUser.setLocalDateTime(LocalDateTime.now());
            userRepo.save(exisitingUser);
        }
        else{
            User user = User.builder()
                    .email(email)
                    .otp(otp)
                    .verified(false)
                    .password("TEMP")
                    .build();

            userRepo.save(user);

        }

        sendEmail(email, otp);
        return "OTP sent to email.";
    }


    public void registerRecruiter(userDTO userDto, recruiterDTO recruiterDto,MultipartFile logoFile) throws  IOException{

        if(userRepo.findByEmail(userDto.getEmail()).isPresent()){
            throw new IllegalArgumentException("Email already Exsists");
        }

        User user=User.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(Role.RECRUITER)
                .build();

        RecruiterProfileDetails recruiter=RecruiterProfileDetails.builder()
                .user(user)
                .companyName(recruiterDto.getCompanyName())
                .companyWebsite(recruiterDto.getCompanyWebsite())
                .companyLogo(logoFile.getBytes())
                .build();


        user.setRecruiterProfileDetails(recruiter);
        userRepo.save(user);


    }


    private void sendEmail(String to,String otp){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("OTP for Email Verification");
        message.setText("Your Otp :"+otp+" \n Valid for 5 Minutes");
        javaMailSender.send(message);

    }


    public String verifyOtp(verifyOtpRequestDTO dto){
        Optional<User> userOtp=userRepo.findByEmail(dto.getEmail());
        if(userOtp.isEmpty()) return "User not found";

        User user=userOtp.get();

        if(user.getOtp().equals(dto.getOtp())){
            user.setVerified(true);
            user.setOtp(null);
            userRepo.save(user);
            return "Email verified Successfully";

        }
        else {
            return "Invalid-OTP!";
        }

    }

}
