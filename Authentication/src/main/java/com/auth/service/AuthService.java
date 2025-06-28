package com.auth.service;

import com.auth.dto.ForgotPasswordRequest;
import com.auth.dto.LoginRequest;
import com.auth.dto.SignupRequest;
import com.auth.entities.User;
import com.auth.jwt.JwtUtils;
import com.auth.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;  // ✅ use reusable service

    @Autowired
    private JwtUtils jwtUtils;

    public Map<String, Object> registerUser(SignupRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if (user.isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setMobile(request.getMobile());
        newUser.setRole(request.getRole());

        userRepository.save(newUser);

        Map<String, Object> response = new HashMap<>();

        response.put("user", newUser);
        return  response;
    }

    public  Map<String, Object> login(@Valid LoginRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if (user.isEmpty()) {
            throw new RuntimeException("Email is not registered");
        }

        User existingUser = user.get();
        if (!passwordEncoder.matches(request.getPassword(),existingUser.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtils.generateToken(existingUser.getEmail());
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", existingUser);

        return response;

    }

    public String forgotPassword(ForgotPasswordRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return "Email is not registered";
        }

        User user = userOpt.get();
        String token = UUID.randomUUID().toString(); // ⚠️ Save this in DB in real apps
        String resetLink = "http://localhost:3000/reset-password?token=" + token;

        String subject = "Reset your password";
        String body = "Hello " + user.getName() + ",\n\n"
                + "Click the following link to reset your password:\n"
                + resetLink + "\n\nIf you didn’t request this, ignore this email.";

        emailService.sendSimpleEmail(user.getEmail(), subject, body); // ✅ reusable method

        return "Password reset link sent to your email";
    }
}
