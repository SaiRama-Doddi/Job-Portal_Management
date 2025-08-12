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
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

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


    @Transactional
    public  void updateJobSeeker(UUID userId, jobSeekerDTO jobSeekerDto, MultipartFile resume) throws IOException {
        Optional<JobSeekerDetails> jb=jobseekerRepo.findByUser_UserId(userId);
        if(jb.isEmpty()) throw new IllegalArgumentException("Job seeker profile not found");

        JobSeekerDetails jobSeekerDetails=jb.get();

        jobSeekerDetails.setCurrentLocation(jobSeekerDto.getCurrentLocation());
        jobSeekerDetails.setPreferredLocation(jobSeekerDto.getPreferredLocation());
        jobSeekerDetails.setExperienceYears(jobSeekerDto.getExperienceYears());

        if(jobSeekerDto.getSkills()!=null){
            String skillsString=String.join(",",jobSeekerDto.getSkills());
            jobSeekerDetails.setSkills(skillsString);
        }

        if(resume!=null && !resume.isEmpty()){
            jobSeekerDetails.setResume(resume.getBytes());
        }
        jobseekerRepo.save(jobSeekerDetails);
    }


    public String sendOtpToEmail(String email) throws MessagingException {
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
                .role(recruiterDto.getRole())
                .build();


       // user.setRecruiterProfileDetails(recruiter);
        recruiter.setUser(user);
        userRepo.save(user);
        recruiterprofileRepo.save(recruiter);


    }


    @Transactional
    public void updateRecruiter(UUID userId,recruiterDTO recruiterDto,MultipartFile companyLogo) throws  IOException{
        //find user by userdy from recruiter
        Optional<RecruiterProfileDetails> rp=recruiterprofileRepo.findByUser_UserId(userId);

        if(rp.isEmpty()) throw new IllegalArgumentException("Recruiter profile not found");

        RecruiterProfileDetails recruiterProfileDetails=rp.get();

        recruiterProfileDetails.setCompanyDescription(recruiterDto.getCompanyDescription());
        recruiterProfileDetails.setCompanyName(recruiterDto.getCompanyName());
        recruiterProfileDetails.setCompanyWebsite(recruiterDto.getCompanyWebsite());
        recruiterProfileDetails.setExperienceNeeded(recruiterDto.getExperienceNeeded());
        recruiterProfileDetails.setRole(recruiterDto.getRole());

        if(companyLogo!=null && !companyLogo.isEmpty()){
            recruiterProfileDetails.setCompanyLogo(companyLogo.getBytes());
        }

        recruiterprofileRepo.save(recruiterProfileDetails);

    }


    private void sendEmail(String to, String otp) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        // true = multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject("OTP for Email Verification");

        // Set From (optional, defaults to configured email)
        helper.setFrom("your-email@example.com");

        // Create an HTML body
        String htmlMsg = "<h3>OTP for Email Verification</h3>"
                + "<p>Your OTP is: <b>" + otp + "</b></p>"
                + "<p>This OTP is valid for <b>5 minutes</b>.</p>"
                + "<br><p>Regards,<br>Job Portal Team</p>";

        helper.setText(htmlMsg, true); // true = isHtml

        // Add custom headers if needed
        message.addHeader("X-Mailer", "JavaMail");
        message.addHeader("X-Priority", "1");  // High priority

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



    public String passwordResetOtp(String email) throws MessagingException {
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
