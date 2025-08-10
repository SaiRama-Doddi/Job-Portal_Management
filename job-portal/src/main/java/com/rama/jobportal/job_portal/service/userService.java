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
import lombok.Data;
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

        if (jobseekerRepo.findByUser(user).isPresent()) {
            throw new IllegalArgumentException("User profile already exists for this email.");
        }


        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(Role.JOB_SEEKER);
       // user.setOtp(userDto.getOtp());
        // Set password only now
        user.setLocalDateTime(LocalDateTime.now());

        String skill = String.join(",", jobSeekerDto.getSkills());

        JobSeekerDetails profile = JobSeekerDetails.builder()
                .user(user)
                .experienceYears(jobSeekerDto.getExperienceYears())
                .currentLocation(jobSeekerDto.getCurrentLocation())
                .preferredLocation(jobSeekerDto.getPreferredLocation())
                .skills(skill)
                .resume(resume.getBytes())
                .build();

       // user.setJobSeekerDetails(profile);
        profile.setUser(user);

        userRepo.save(user);
        jobseekerRepo.save(profile);
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
            System.out.println("Saving user with OTP: " + otp + " at " + LocalDateTime.now());

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


    public void registerRecruiter(userDTO userDto, recruiterDTO recruiterDto,MultipartFile companyLogo) throws  IOException{

        Optional<User> optionalUser = userRepo.findByEmail(userDto.getEmail());
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("Please verify your email before registering.");
        }

        User user = optionalUser.get();

        if (!user.isVerified()) {
            throw new IllegalArgumentException("Email not verified. Please verify OTP before registering.");
        }

        // Check if recruiter profile already exists for this user
        if (recruiterprofileRepo.findByUser(user).isPresent()) {
            throw new IllegalArgumentException("Recruiter profile already exists for this email.");
        }

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(Role.RECRUITER);
       // user.setOtp(userDto.getOtp());
        user.setLocalDateTime(LocalDateTime.now());

        RecruiterProfileDetails recruiter=RecruiterProfileDetails.builder()
                .user(user)
                .companyName(recruiterDto.getCompanyName())
                .companyWebsite(recruiterDto.getCompanyWebsite())
                .companyLogo(companyLogo.getBytes())
                .companyDescription(recruiterDto.getCompanyDescription())
                .experienceNeeded(recruiterDto.getExperienceNeeded())
                .build();


       // user.setRecruiterProfileDetails(recruiter);
        recruiter.setUser(user);
        userRepo.save(user);
        recruiterprofileRepo.save(recruiter);


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



    public String passwordResetOtp(String email){
        Optional<User> optionalUser=userRepo.findByEmail(email);
        if(optionalUser.isEmpty()){
            throw new IllegalArgumentException("user with the email deosnt exists");
        }
        User user=optionalUser.get();

        String otp=String.valueOf(new Random().nextInt(900000)+100000);
        user.setOtp(otp);
        user.setVerified(false);
        user.setLocalDateTime(LocalDateTime.now());
        userRepo.save(user);
        sendEmail(email,otp);
        return "Otp sent for password reset.";
    }

    public String verifyPasswordResetOption(verifyOtpRequestDTO dto){
        Optional<User> optionalUser=userRepo.findByEmail(dto.getEmail());
        if(optionalUser.isEmpty()) return "User not found";
        User user=optionalUser.get();

        if (user.getOtp() != null && user.getOtp().equals(dto.getOtp())) {
            user.setVerified(true);  // Mark as verified for password reset
            user.setOtp(null);       // Clear OTP after successful verification
            userRepo.save(user);
            return "OTP verified successfully. You can now reset your password.";
        } else {
            return "Invalid OTP!";
        }

    }

    // Reset password once OTP is verified
    public String resetPassword(String email,String newPassword){
        Optional<User> optionalUser=userRepo.findByEmail(email);
        if(optionalUser.isEmpty()) return "User not found";
        User user=optionalUser.get();
        if (!user.isVerified()) {
            return "OTP not verified. Please verify OTP before resetting password.";
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setVerified(false);  // Reset the verified flag after password reset (optional)
        userRepo.save(user);

        return "Password reset successfully.";
    }


}
