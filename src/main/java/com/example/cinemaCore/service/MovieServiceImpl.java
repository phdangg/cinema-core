package com.example.cinemaCore.service;

import com.example.cinemaCore.dto.MovieDto;
import com.example.cinemaCore.dto.MoviePageResponse;
import com.example.cinemaCore.entities.Movie;
import com.example.cinemaCore.exceptions.FileDeleteException;
import com.example.cinemaCore.exceptions.MovieNotFoundException;
import com.example.cinemaCore.mapper.MovieMapper;
import com.example.cinemaCore.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
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
        if (Files.exists(Paths.get(path + File.separator + file.getOriginalFilename()))) {
            throw new FileAlreadyExistsException("File already exists! Please enter another file name");
        }
        String uploadFileName = fileService.uploadFile(path,file);
        movieDto.setPoster(uploadFileName);

        Movie savedMovie = movieRepository.save(MovieMapper.mapToMovieEntity(movieDto));

        String posterUrl = baseUrl + "/file/" + uploadFileName;

        return MovieMapper.mapToMovieDto(savedMovie,posterUrl);
    }

    @Override
    public MovieDto getMovie(Integer movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(()->new MovieNotFoundException("Movie not found with id = "+ movieId));

        String posterUrl = baseUrl + "/file/" + movie.getPoster();
        return MovieMapper.mapToMovieDto(movie,posterUrl);
    }

    @Override
    public List<MovieDto> getAllMovies() {
        List<Movie> all = movieRepository.findAll();
        List<MovieDto> movieDtos = new ArrayList<>();
        for (Movie movie : all){
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            movieDtos.add(MovieMapper.mapToMovieDto(movie,posterUrl));
        }

        return movieDtos;
    }

    @Override
    public MovieDto updateMovie(Integer movieId, MovieDto movieDto, MultipartFile file) throws IOException {
        Movie movie = movieRepository.findById(movieId).orElseThrow(()->new MovieNotFoundException("Movie not found with id = " + movieId ));
        String fileName = movie.getPoster();
        if (!file.isEmpty()) {
           Files.deleteIfExists(Paths.get(path + File.separator + fileName));
           fileName = fileService.uploadFile(path,file);
        }
        String posterUrl = baseUrl + "/file/" + fileName;
        movieDto.setPoster(fileName);
        movieDto.setPosterUrl(posterUrl);
        Movie updatedMovie = movieRepository.save(MovieMapper.mapToMovieEntity(movieDto));

        return MovieMapper.mapToMovieDto(updatedMovie,posterUrl);
    }

    @Override
    public String deleteMovie(Integer movieId) throws IOException, FileDeleteException {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(()-> new MovieNotFoundException("Movie not found with id = "+ movieId));
        try {
            Files.deleteIfExists(Paths.get(path+File.separator+movie.getPoster()));
            movieRepository.delete(movie);
        } catch (FileSystemException e) {
            throw new FileDeleteException("File is in use and cannot be deleted: " + e.getMessage());
        }
        return "Movie deleted with id = " + movieId;
    }

    @Override
    public MoviePageResponse getAllMovieWithPagination(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize);

        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movies = moviePages.getContent();

        List<MovieDto> movieDtos = new ArrayList<>();
        for (Movie movie: movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            movieDtos.add(MovieMapper.mapToMovieDto(movie,posterUrl));
        }

        return new MoviePageResponse(
                movieDtos,
                pageNumber,
                pageSize,
                moviePages.getTotalElements(),
                moviePages.getTotalPages(),
                moviePages.isLast()
        );
    }

    @Override
    public MoviePageResponse getAllMovieWithPaginationWithSorting(Integer pageNumber, Integer pageSize, String sortBy, String dir) {
        Sort sort = dir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Movie> moviePages = movieRepository.findAll(pageable);
        List<Movie> movies = moviePages.getContent();

        List<MovieDto> movieDtos = new ArrayList<>();
        for (Movie movie: movies) {
            String posterUrl = baseUrl + "/file/" + movie.getPoster();
            movieDtos.add(MovieMapper.mapToMovieDto(movie,posterUrl));
        }

        return new MoviePageResponse(
                movieDtos,
                pageNumber,
                pageSize,
                moviePages.getTotalElements(),
                moviePages.getTotalPages(),
                moviePages.isLast()
        );
    }
}
