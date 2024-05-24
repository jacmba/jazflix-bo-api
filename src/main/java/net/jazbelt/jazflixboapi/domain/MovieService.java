package net.jazbelt.jazflixboapi.domain;

import net.jazbelt.jazflixboapi.error.MovieNotFoundException;
import net.jazbelt.jazflixboapi.model.entity.Movie;
import net.jazbelt.jazflixboapi.model.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService implements IMovieService {

    private final MovieRepository repository;

    @Autowired
    public MovieService(MovieRepository repository) {
        this.repository = repository;
    }

    @Override
    public Movie createMovie(Movie movie) {
        movie.setId(null);
        return repository.save(movie);
    }

    @Override
    public List<Movie> retrieveAllMovies() {
        return repository.findAll();
    }

    @Override
    public Movie retrieveSingleMovie(String id) {
        Optional<Movie> result = repository.findById(id);

        if (result.isEmpty()) {
            throw new MovieNotFoundException(id);
        }

        return result.get();
    }

    @Override
    public void updateMovie(String id, Movie movie) {
        retrieveSingleMovie(id);
        movie.setId(id);
        repository.save(movie);
    }

    @Override
    public void deleteMovie(String id) {
        retrieveSingleMovie(id);
        repository.deleteById(id);
    }
}
