package com.example.cinemaCore.auth.repositories;

import com.example.cinemaCore.auth.entities.ForgotPassword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {
}
