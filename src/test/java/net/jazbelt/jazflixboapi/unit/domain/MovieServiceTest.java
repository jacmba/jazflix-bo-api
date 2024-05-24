package net.jazbelt.jazflixboapi.unit.domain;

import net.jazbelt.jazflixboapi.domain.IMovieService;
import net.jazbelt.jazflixboapi.domain.MovieService;
import net.jazbelt.jazflixboapi.error.MovieNotFoundException;
import net.jazbelt.jazflixboapi.model.entity.Movie;
import net.jazbelt.jazflixboapi.model.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    IMovieService service;

    @Mock
    MovieRepository repository;

    @BeforeEach
    void setUp() {
        service = new MovieService(repository);

        Movie movie1 = new Movie(
                "1",
                "Movie 1",
                "First movie",
                "http://movies.info/1.png",
                "movie1.mp4",
                "tag1,tag2"
        );

        Movie movie2 = new Movie(
                "2",
                "Movie 2",
                null,
                "http://movies.info/2.png",
                "movie2.mp4",
                null
        );

        Movie movie3 = new Movie(
                "3",
                "Movie 3",
                "Third movie",
                "http://movies.info/3.png",
                "movie3.mp4",
                "tag1"
        );

        lenient().when(repository.findAll()).thenReturn(Arrays.asList(movie1, movie2, movie3));
        lenient().when(repository.findById("1")).thenReturn(Optional.of(movie1));
        lenient().when(repository.findById("x")).thenReturn(Optional.empty());
        lenient().when(repository.save(any(Movie.class))).thenReturn(movie1);
    }

    @Test
    void retrieveAllMoviesShouldReturnAllEntities() {
        List<Movie> movies = service.retrieveAllMovies();

        verify(repository).findAll();

        assertNotNull(movies);
        assertEquals(3, movies.size());

        Movie movie1 = movies.get(0);
        assertEquals("1", movie1.getId());
        assertEquals("Movie 1", movie1.getTitle());
        assertEquals("First movie", movie1.getDescription());
        assertEquals("http://movies.info/1.png", movie1.getImage());
        assertEquals("movie1.mp4", movie1.getVideo());
        assertEquals("tag1,tag2", movie1.getExtra());

        Movie movie2 = movies.get(1);
        assertEquals("2", movie2.getId());
        assertEquals("Movie 2", movie2.getTitle());
        assertNull(movie2.getDescription());
        assertEquals("http://movies.info/2.png", movie2.getImage());
        assertEquals("movie2.mp4", movie2.getVideo());
        assertNull(movie2.getExtra());

        Movie movie3 = movies.get(2);
        assertEquals("3", movie3.getId());
        assertEquals("Movie 3", movie3.getTitle());
        assertEquals("Third movie", movie3.getDescription());
        assertEquals("http://movies.info/3.png", movie3.getImage());
        assertEquals("movie3.mp4", movie3.getVideo());
        assertEquals("tag1", movie3.getExtra());
    }

    @Test
    void retrieveSingleMovieShouldReturnFirstMovie() {
        Movie movie = service.retrieveSingleMovie("1");

        verify(repository).findById("1");

        assertNotNull(movie);
        assertEquals("1", movie.getId());
        assertEquals("Movie 1", movie.getTitle());
        assertEquals("First movie", movie.getDescription());
        assertEquals("http://movies.info/1.png", movie.getImage());
        assertEquals("movie1.mp4", movie.getVideo());
        assertEquals("tag1,tag2", movie.getExtra());
    }

    @Test
    void retrieveNonExistingSingleMovieShouldThrowNotFoundException() {
        MovieNotFoundException ex = assertThrows(MovieNotFoundException.class, () ->
                service.retrieveSingleMovie("x")
        );

        verify(repository).findById("x");
        assertEquals("Movie [x] not found", ex.getMessage());
    }

    @Test
    void createNewMovieShouldReturnCreatedMovie() {
        Movie input = new Movie(
                null,
                "Movie 1",
                "First movie",
                "http://movies.info/1.png",
                "movie1.mp4",
                "tag1,tag2"
        );

        Movie result = service.createMovie(input);

        verify(repository).save(input);
        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Movie 1", result.getTitle());
        assertEquals("First movie", result.getDescription());
        assertEquals("http://movies.info/1.png", result.getImage());
        assertEquals("movie1.mp4", result.getVideo());
        assertEquals("tag1,tag2", result.getExtra());
    }

    @Test
    void createMovieShouldAlwaysSaveEntityWithNullId() {
        Movie input = new Movie(
                "aabbcc112233",
                "Movie 1",
                "First movie",
                "http://movies.info/1.png",
                "movie1.mp4",
                "tag1,tag2"
        );

        Movie expect = new Movie(
                null,
                "Movie 1",
                "First movie",
                "http://movies.info/1.png",
                "movie1.mp4",
                "tag1,tag2"
        );

        service.createMovie(input);
        verify(repository).save(expect);
    }

    @Test
    void updateMovieShouldInvokeRepositorySave() {
        Movie input = new Movie(
                "1",
                "Movie 1",
                "First movie",
                "http://movies.info/1.png",
                "movie1.mp4",
                "tag1,tag2"
        );

        service.updateMovie("1", input);
        verify(repository).save(input);
    }

    @Test
    void updateMovieShouldUseSpecifiedIDByParam() {
        Movie input = new Movie(
                "xxyyzz445566",
                "Movie 1",
                "My first movie",
                "http://movies.info/1.png",
                "movie1.mp4",
                "tag1,tag2,tag3"
        );

        Movie expect = new Movie(
                "1",
                "Movie 1",
                "My first movie",
                "http://movies.info/1.png",
                "movie1.mp4",
                "tag1,tag2,tag3"
        );

        service.updateMovie("1", input);
        verify(repository).save(expect);
    }

    @Test
    void updateNonExistingMovieShouldThrowNotFoundException() {
        MovieNotFoundException ex = assertThrows(MovieNotFoundException.class, () -> {
            Movie input = new Movie(
                    "x",
                    "Movie 1",
                    "First movie",
                    "http://movies.info/1.png",
                    "movie1.mp4",
                    "tag1,tag2"
            );

            service.updateMovie("x", input);
        });

        verify(repository).findById("x");
        assertEquals("Movie [x] not found", ex.getMessage());
    }

    @Test
    void deleteMovieShouldInvokeRepositoryRemoval() {
        service.deleteMovie("1");
        verify(repository).deleteById("1");
    }

    @Test
    void deleteNonExistingMovieShouldThrowNotFoundError() {
        MovieNotFoundException ex = assertThrows(MovieNotFoundException.class, () ->
                service.deleteMovie("abcdefg789")
        );

        verify(repository).findById("abcdefg789");
        assertEquals("Movie [abcdefg789] not found", ex.getMessage());
    }
}
