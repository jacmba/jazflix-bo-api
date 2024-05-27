package net.jazbelt.jazflixboapi.unit.controller;

import net.jazbelt.jazflixboapi.controller.MovieController;
import net.jazbelt.jazflixboapi.domain.MovieService;
import net.jazbelt.jazflixboapi.model.entity.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieControllerTest {

    MovieController controller;

    @Mock
    MovieService service;

    @BeforeEach
    void setUp() {
        controller = new MovieController(service);

        Movie movie1 = new Movie("1", "Movie 1", "first", "1.png", "1.mp4", "tag1");
        Movie movie2 = new Movie("2", "Movie 2", "second", "2.png", "2.mp4", "tag1");
        Movie movie3 = new Movie("3", "Movie 3", "third", "3.png", "3.mp4", "tag1");

        lenient().when(service.retrieveAllMovies()).thenReturn(Arrays.asList(movie1, movie2, movie3));
        lenient().when(service.retrieveSingleMovie("1")).thenReturn(movie1);
        lenient().when(service.retrieveSingleMovie("2")).thenReturn(movie2);
        lenient().when(service.retrieveSingleMovie("3")).thenReturn(movie3);
        lenient().when(service.createMovie(any(Movie.class))).thenReturn(movie1);
    }

    @Test
    void getAllMoviesShouldReturnListOf3Movies() {
        List<Movie> movies = controller.getMovies();

        verify(service).retrieveAllMovies();

        assertNotNull(movies);

        Movie movie1 = movies.get(0);
        assertEquals("1", movie1.getId());
        assertEquals("Movie 1", movie1.getTitle());
        assertEquals("first", movie1.getDescription());
        assertEquals("1.png", movie1.getImage());
        assertEquals("1.mp4", movie1.getVideo());
        assertEquals("tag1", movie1.getExtra());

        Movie movie2 = movies.get(1);
        assertEquals("2", movie2.getId());
        assertEquals("Movie 2", movie2.getTitle());
        assertEquals("second", movie2.getDescription());
        assertEquals("2.png", movie2.getImage());
        assertEquals("2.mp4", movie2.getVideo());
        assertEquals("tag1", movie2.getExtra());

        Movie movie3 = movies.get(2);
        assertEquals("3", movie3.getId());
        assertEquals("Movie 3", movie3.getTitle());
        assertEquals("third", movie3.getDescription());
        assertEquals("3.png", movie3.getImage());
        assertEquals("3.mp4", movie3.getVideo());
        assertEquals("tag1", movie3.getExtra());
    }

    @Test
    void getMovieShouldReturnFirstMovie() {
        Movie movie = controller.getMovie("1");

        verify(service).retrieveSingleMovie("1");
        assertNotNull(movie);
        assertEquals("1", movie.getId());
        assertEquals("Movie 1", movie.getTitle());
        assertEquals("first", movie.getDescription());
        assertEquals("1.png", movie.getImage());
        assertEquals("1.mp4", movie.getVideo());
        assertEquals("tag1", movie.getExtra());
    }

    @Test
    void postNewMovieShouldReturnCreatedMovie() {
        Movie input = new Movie();

        Movie movie = controller.postNewMovie(input);

        verify(service).createMovie(input);
        assertNotNull(movie);
        assertEquals("1", movie.getId());
        assertEquals("Movie 1", movie.getTitle());
        assertEquals("first", movie.getDescription());
        assertEquals("1.png", movie.getImage());
        assertEquals("1.mp4", movie.getVideo());
        assertEquals("tag1", movie.getExtra());
    }

    @Test
    void putUpdateMovieShouldInvokeServiceMethod() {
        controller.putMovie("1", new Movie());
        verify(service).updateMovie("1", new Movie());
    }

    @Test
    void deleteMovieShouldInvokeServiceMethod() {
        controller.deleteMovie("1");
        verify(service).deleteMovie("1");

        controller.deleteMovie("abc123");
        verify(service).deleteMovie("abc123");
    }
}
