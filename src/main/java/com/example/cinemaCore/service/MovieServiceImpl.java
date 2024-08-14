package com.example.cinemaCore.service;

import com.example.cinemaCore.dto.MovieDto;
import com.example.cinemaCore.entities.Movie;
import com.example.cinemaCore.mapper.MovieMapper;
import com.example.cinemaCore.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService{

    private final MovieRepository movieRepository;

    private final FileService fileService;

    @Value("${project.poster}")
    private String path;

    @Value("${base.url}")
    private String baseUrl;

    public MovieServiceImpl(MovieRepository movieRepository, FileService fileService) {
        this.movieRepository = movieRepository;
        this.fileService = fileService;
    }

    @Override
    public MovieDto addMovie(MovieDto movieDto, MultipartFile file) throws IOException {
        String uploadFileName = fileService.uploadFile(path,file);
        movieDto.setPoster(uploadFileName);

        Movie savedMovie = MovieMapper.mapToMovieEntity(movieDto);

        String posterUrl = baseUrl + "/file/" + uploadFileName;

        return MovieMapper.mapToMovieDto(savedMovie,posterUrl);
    }

    @Override
    public MovieDto getMovie(Integer movieId) {
        return null;
    }

    @Override
    public List<MovieDto> getAllMovies() {
        return List.of();
    }
}
