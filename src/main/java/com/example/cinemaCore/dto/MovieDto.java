package com.example.cinemaCore.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {
    private Integer movieId;

    @NotBlank(message = "Movie's title should not be empty")
    private String title;

    @NotBlank(message = "Movie's director should not be empty")
    private String director;

    @NotBlank(message = "Movie's studio should not be empty")
    private String studio;

    private Set<String> movieCast;

    private Integer releaseYear;

    @NotBlank(message = "Movie's poster should not be empty")
    private String poster;

    @NotBlank(message = "Movie's poster should not be empty")
    private String posterUrl;
}
