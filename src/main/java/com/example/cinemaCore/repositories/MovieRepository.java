package com.example.cinemaCore.repositories;

import com.example.cinemaCore.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {
}
