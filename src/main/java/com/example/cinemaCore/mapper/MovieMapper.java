package com.example.cinemaCore.mapper;

import com.example.cinemaCore.dto.MovieDto;
import com.example.cinemaCore.entities.Movie;

public class MovieMapper {
    public static Movie mapToMovieEntity(MovieDto movieDto) {
        return new Movie(
                movieDto.getMovieId(),
                movieDto.getTitle(),
                movieDto.getDirector(),
                movieDto.getStudio(),
                movieDto.getMovieCast(),
                movieDto.getReleaseYear(),
                movieDto.getPoster()
        );
    }
    public static MovieDto mapToMovieDto(Movie movie, String posterUrl) {
        return new MovieDto(
                movie.getMovieId(),
                movie.getTitle(),
                movie.getDirector(),
                movie.getStudio(),
                movie.getMovieCast(),
                movie.getReleaseYear(),
                movie.getPoster(),
                posterUrl
        );
    }
}
