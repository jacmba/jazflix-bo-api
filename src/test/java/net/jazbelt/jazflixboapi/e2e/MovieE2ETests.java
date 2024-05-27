package net.jazbelt.jazflixboapi.e2e;

import net.jazbelt.jazflixboapi.model.entity.Movie;
import net.jazbelt.jazflixboapi.model.repository.MovieRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class MovieE2ETests {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    RestTemplate restTemplate;

    @LocalServerPort
    int port;

    @Autowired
    MovieRepository repository;

    private String baseUri;

    private static String movieId;

    @BeforeEach
    void setUp() {
        baseUri = String.format("http://localhost:%d/movies", port);
    }

    @Test
    @Order(1)
    void getMoviesShouldReturnAllMovies() {
        Movie[] movies = restTemplate.getForObject(baseUri, Movie[].class);

        assertNotNull(movies);
        assertEquals(3, movies.length);

        Movie movie1 = movies[0];
        assertNotNull(movie1.getId());
        assertEquals("Movie 1", movie1.getTitle());
        assertEquals("first", movie1.getDescription());
        assertEquals("1.png", movie1.getImage());
        assertEquals("1.mp4", movie1.getVideo());
        assertEquals("tag1", movie1.getExtra());

        Movie movie2 = movies[1];
        assertNotNull(movie2.getId());
        assertEquals("Movie 2", movie2.getTitle());
        assertEquals("second", movie2.getDescription());
        assertEquals("2.png", movie2.getImage());
        assertEquals("2.mp4", movie2.getVideo());
        assertEquals("tag1", movie2.getExtra());

        Movie movie3 = movies[2];
        assertNotNull(movie3.getId());
        assertEquals("Movie 3", movie3.getTitle());
        assertEquals("third", movie3.getDescription());
        assertEquals("3.png", movie3.getImage());
        assertEquals("3.mp4", movie3.getVideo());
        assertEquals("tag1", movie3.getExtra());

        movieId = movie1.getId();
    }

    @Test
    @Order(2)
    void getMovieShouldReturnFirstMovie() {
        String uri = baseUri + "/" + movieId;
        Movie movie = restTemplate.getForObject(uri, Movie.class);

        assertNotNull(movie);
        assertEquals(movieId, movie.getId());
        assertEquals("Movie 1", movie.getTitle());
        assertEquals("first", movie.getDescription());
        assertEquals("1.png", movie.getImage());
        assertEquals("1.mp4", movie.getVideo());
        assertEquals("tag1", movie.getExtra());
    }

    @Test
    @Order(3)
    void getNonExistingMovieShouldThrowNotFoundError() {
        HttpClientErrorException.NotFound ex = assertThrows(HttpClientErrorException.NotFound.class, () ->
                restTemplate.getForObject(baseUri + "/abc789", Movie.class)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    @Order(4)
    void postNewMovieShouldReturnCreatedObject() {
        Movie input = new Movie(null, "Test Movie", "test", "http://test.info/test.png", "test.mp4", "test1");
        Movie movie = restTemplate.postForObject(baseUri, input, Movie.class);

        assertNotNull(movie);
        assertNotNull(movie.getId());
        assertEquals("Test Movie", movie.getTitle());
        assertEquals("test", movie.getDescription());
        assertEquals("http://test.info/test.png", movie.getImage());
        assertEquals("test.mp4", movie.getVideo());
        assertEquals("test1", movie.getExtra());

        List<Movie> movies = repository.findAll();

        assertEquals(4, movies.size());
        assertEquals(movie, movies.get(3));

        movieId = movie.getId();
    }

    @Test
    @Order(5)
    void postNewMovieWithNullTitleShouldThrowBadRequestError() {
        Movie input = new Movie(null, null, "test", "http://test.info/test.png", "test.mp4", "test1");
        HttpClientErrorException.BadRequest ex = assertThrows(HttpClientErrorException.BadRequest.class, () ->
                restTemplate.postForObject(baseUri, input, Movie.class)
        );

        long total = repository.count();

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals(4L, total);
    }

    @Test
    @Order(6)
    void postNewMovieWithInvaliditleShouldThrowBadRequestError() {
        Movie input = new Movie(null, "Test", "test", "http://test.info/test.png", "test.mp4", "test1");
        HttpClientErrorException.BadRequest ex = assertThrows(HttpClientErrorException.BadRequest.class, () ->
                restTemplate.postForObject(baseUri, input, Movie.class)
        );

        long total = repository.count();

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals(4L, total);
    }

    @Test
    @Order(7)
    void postNewMovieWithNullImageShouldThrowBadRequestError() {
        Movie input = new Movie(null, "Test Movie", "test", null, "test.mp4", "test1");
        HttpClientErrorException.BadRequest ex = assertThrows(HttpClientErrorException.BadRequest.class, () ->
                restTemplate.postForObject(baseUri, input, Movie.class)
        );

        long total = repository.count();

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals(4L, total);
    }

    @Test
    @Order(8)
    void postNewMovieWithInvalidImageURLShouldThrowBadRequestError() {
        Movie input = new Movie(null, "Test Movie", "test", "image.png", "test.mp4", "test1");
        HttpClientErrorException.BadRequest ex = assertThrows(HttpClientErrorException.BadRequest.class, () ->
                restTemplate.postForObject(baseUri, input, Movie.class)
        );

        long total = repository.count();

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals(4L, total);
    }

    @Test
    @Order(9)
    void postNewMovieWithNullVideoShouldThrowBadRequestError() {
        Movie input = new Movie(null, "Test Movie", "test", "http://test.info/test.png", null, "test1");
        HttpClientErrorException.BadRequest ex = assertThrows(HttpClientErrorException.BadRequest.class, () ->
                restTemplate.postForObject(baseUri, input, Movie.class)
        );

        long total = repository.count();

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals(4L, total);
    }

    @Test
    @Order(10)
    void postNewMovieWithInvalidVideoShouldThrowBadRequestError() {
        Movie input = new Movie(null, "Test Movie", "test", "http://test.info/test.png", "1.m4", "test1");
        HttpClientErrorException.BadRequest ex = assertThrows(HttpClientErrorException.BadRequest.class, () ->
                restTemplate.postForObject(baseUri, input, Movie.class)
        );

        long total = repository.count();

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals(4L, total);
    }

    @Test
    @Order(11)
    void putMovieShouldReflectChanges() {
        Movie input = new Movie(movieId, "Test Changed Movie", "test", "http://test.info/test.png", "test.mp4", "test1,test2");
        restTemplate.put(baseUri + "/" + movieId, input);

        List<Movie> movies = repository.findAll();

        assertEquals(4, movies.size());
        assertEquals(input, movies.get(3));
    }

    @Test
    @Order(12)
    void putNonExistingMovieShouldTriggerNotFoundException() {
        Movie input = new Movie(movieId, "Test Not Changed Movie", "test", "http://test.info/test.png", "test.mp4", "test1,test2");
        HttpClientErrorException.NotFound ex = assertThrows(HttpClientErrorException.NotFound.class, () ->
                restTemplate.put(baseUri + "/abadfdfdasfas", input)
    );

        List<Movie> movies = repository.findAll();

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals(4, movies.size());
        assertNotEquals(input, movies.get(3));
    }

    @Test
    @Order(13)
    void putMovieWithNullTitleShouldTriggerBadRequestException() {
        Movie input = new Movie(movieId, null, "test", "http://test.info/test.png", "test.mp4", "test1,test2");
        HttpClientErrorException.BadRequest ex = assertThrows(HttpClientErrorException.BadRequest.class, () ->
                restTemplate.put(baseUri + "/" + movieId, input)
        );

        List<Movie> movies = repository.findAll();

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals(4, movies.size());
        assertNotEquals(input, movies.get(3));
    }

    @Test
    @Order(14)
    void putMovieWithInvalidTitleShouldTriggerBadRequestException() {
        Movie input = new Movie(movieId, "Test", "test", "http://test.info/test.png", "test.mp4", "test1,test2");
        HttpClientErrorException.BadRequest ex = assertThrows(HttpClientErrorException.BadRequest.class, () ->
                restTemplate.put(baseUri + "/" + movieId, input)
        );

        List<Movie> movies = repository.findAll();

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals(4, movies.size());
        assertNotEquals(input, movies.get(3));
    }

    @Test
    @Order(15)
    void putMovieWithNullImageShouldTriggerBadRequestException() {
        Movie input = new Movie(movieId, "Test Movie", "test", null, "test.mp4", "test1,test2");
        HttpClientErrorException.BadRequest ex = assertThrows(HttpClientErrorException.BadRequest.class, () ->
                restTemplate.put(baseUri + "/" + movieId, input)
        );

        List<Movie> movies = repository.findAll();

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals(4, movies.size());
        assertNotEquals(input, movies.get(3));
    }

    @Test
    @Order(16)
    void putMovieWithInvalidImageURLShouldTriggerBadRequestException() {
        Movie input = new Movie(movieId, "Test Movie", "test", "image.png", "test.mp4", "test1,test2");
        HttpClientErrorException.BadRequest ex = assertThrows(HttpClientErrorException.BadRequest.class, () ->
                restTemplate.put(baseUri + "/" + movieId, input)
        );

        List<Movie> movies = repository.findAll();

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals(4, movies.size());
        assertNotEquals(input, movies.get(3));
    }

    @Test
    @Order(17)
    void putMovieWithNullVideoShouldTriggerBadRequestException() {
        Movie input = new Movie(movieId, "Test Movie", "test", "http://test.info/test.png", null, "test1,test2");
        HttpClientErrorException.BadRequest ex = assertThrows(HttpClientErrorException.BadRequest.class, () ->
                restTemplate.put(baseUri + "/" + movieId, input)
        );

        List<Movie> movies = repository.findAll();

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals(4, movies.size());
        assertNotEquals(input, movies.get(3));
    }

    @Test
    @Order(18)
    void putMovieWithInvalidVideoShouldTriggerBadRequestException() {
        Movie input = new Movie(movieId, "Test Movie", "test", "http://test.info/test.png", "1.mp", "test1,test2");
        HttpClientErrorException.BadRequest ex = assertThrows(HttpClientErrorException.BadRequest.class, () ->
                restTemplate.put(baseUri + "/" + movieId, input)
        );

        List<Movie> movies = repository.findAll();

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals(4, movies.size());
        assertNotEquals(input, movies.get(3));
    }

    @Test
    @Order(19)
    void deleteMovieShouldRemoveRecord() {
        restTemplate.delete(baseUri + "/" + movieId);

        Optional<Movie> movie = repository.findById(movieId);
        long total = repository.count();

        assertTrue(movie.isEmpty());
        assertEquals(3L, total);
    }

    @Test
    @Order(20)
    void deleteNonExistingMovieShouldTriggerNotFoundError() {
        HttpClientErrorException.NotFound ex = assertThrows(HttpClientErrorException.NotFound.class, () ->
                restTemplate.delete(baseUri + "/kfljsr49e85uwiojd")
        );

        long total = repository.count();

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals(3L, total);
    }
}
