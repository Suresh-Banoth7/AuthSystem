package com.auth.service;

import com.auth.dto.ForgotPasswordRequest;
import com.auth.dto.LoginRequest;
import com.auth.dto.SignupRequest;
import com.auth.entities.User;
import com.auth.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;  // ✅ use reusable service

    public String registerUser(SignupRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if (user.isPresent()) {
            return "Email already exists";
        }

        User newUser = new User();
        newUser.setName(request.getName());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(request.getPassword()); // 🔐 Hash later
        newUser.setMobile(request.getMobile());

        userRepository.save(newUser);
        return "User registered successfully";
    }

    public String login(@Valid LoginRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());

        if (user.isEmpty()) {
            return "Email is not registered";
        }

        User existingUser = user.get();
        if (!request.getPassword().equals(existingUser.getPassword())) {
            return "Invalid password";
        }

        return "Login successful";
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
