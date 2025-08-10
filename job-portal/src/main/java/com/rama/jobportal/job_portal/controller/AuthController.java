package com.rama.jobportal.job_portal.controller;

import com.rama.jobportal.job_portal.entity.Role;
import com.rama.jobportal.job_portal.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    @PostMapping("/login")
    public Map<String,String> login(@RequestBody Map<String,String> request){
        try{

            Authentication authentication=authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      request.get("email"),
                      request.get("password")

              )
            );

            UserDetails userDetails=(UserDetails) authentication.getPrincipal();
            String role=userDetails.getAuthorities().iterator().next().getAuthority();
            String token= jwtUtil.generateToken(userDetails.getUsername(), Role.valueOf(role));
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return response;

        }
        catch (AuthenticationException e){
            throw new RuntimeException("Invalid credentials");
        }
    }

    @GetMapping("/api/protected/hello")
    public String hello() {
        return "Hello — you are authenticated!";
    }
}
