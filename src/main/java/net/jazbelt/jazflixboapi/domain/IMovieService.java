package net.jazbelt.jazflixboapi.domain;

import net.jazbelt.jazflixboapi.model.entity.Movie;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IMovieService {

    Movie createMovie(Movie movie);

    List<Movie> retrieveAllMovies();

    Movie retrieveSingleMovie(String id);

    void updateMovie(String id, Movie movie);

    void deleteMovie(String id);
}
