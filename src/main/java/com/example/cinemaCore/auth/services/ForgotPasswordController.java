package com.example.cinemaCore.auth.services;

import com.example.cinemaCore.auth.entities.ForgotPassword;
import com.example.cinemaCore.auth.entities.User;
import com.example.cinemaCore.auth.repositories.ForgotPasswordRepository;
import com.example.cinemaCore.auth.repositories.UserRepository;
import com.example.cinemaCore.dto.MailBody;
import com.example.cinemaCore.exceptions.EmailNotFoundException;
import com.example.cinemaCore.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("/forgotPassword")
public class ForgotPasswordController {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ForgotPasswordRepository forgotPasswordRepository;

    public ForgotPasswordController(UserRepository userRepository, EmailService emailService, ForgotPasswordRepository forgotPasswordRepository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.forgotPasswordRepository = forgotPasswordRepository;
    }

    @PostMapping("/verifyMail/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email) throws EmailNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException("Please enter a valid email"));
        int otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("Your otp code: " + otp)
                .subject("Forgot password")
                .build();
        ForgotPassword fp = ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis()+70*1000))
                .user(user)
                .build();
        emailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(fp);
        return ResponseEntity.ok("Email sent for verification");

    }
    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100_000,999_999);
    }

}
