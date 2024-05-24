package net.jazbelt.jazflixboapi.integration.repository;

import net.jazbelt.jazflixboapi.model.entity.Movie;
import net.jazbelt.jazflixboapi.model.repository.MovieRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataMongoTest()
public class MovieRepositoryTest {

    @Container
    static MongoDBContainer container = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", container::getReplicaSetUrl);
    }

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    MovieRepository repository;

    private String movieId;

    @BeforeEach
    void setUp() {
        Movie movie1 = new Movie(
                null,
                "Movie 1",
                "First movie",
                "http://movies.info/1.png",
                "movie1.mp4",
                "tag1,tag2"
        );

        Movie movie2 = new Movie(
                null,
                "Movie 2",
                null,
                "http://movies.info/2.png",
                "movie2.mp4",
                null
        );

        Movie movie3 = new Movie(
                null,
                "Movie 3",
                "Third movie",
                "http://movies.info/3.png",
                "movie3.mp4",
                "tag1"
        );

        movieId = mongoTemplate.save(movie1).getId();
        mongoTemplate.save(movie2);
        mongoTemplate.save(movie3);
    }

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection(Movie.class);
    }

    @Test
    void findAllShouldReturnAllMovies() {
        List<Movie> movies = repository.findAll();

        assertNotNull(movies);
        assertEquals(3, movies.size());

        Movie movie1 = movies.get(0);
        assertEquals(movieId, movie1.getId());
        assertEquals("Movie 1", movie1.getTitle());
        assertEquals("First movie", movie1.getDescription());
        assertEquals("http://movies.info/1.png", movie1.getImage());
        assertEquals("movie1.mp4", movie1.getVideo());
        assertEquals("tag1,tag2", movie1.getExtra());

        Movie movie2 = movies.get(1);
        assertNotNull(movie2.getId());
        assertEquals("Movie 2", movie2.getTitle());
        assertNull(movie2.getDescription());
        assertEquals("http://movies.info/2.png", movie2.getImage());
        assertEquals("movie2.mp4", movie2.getVideo());
        assertNull(movie2.getExtra());

        Movie movie3 = movies.get(2);
        assertNotNull(movie3.getId());
        assertEquals("Movie 3", movie3.getTitle());
        assertEquals("Third movie", movie3.getDescription());
        assertEquals("http://movies.info/3.png", movie3.getImage());
        assertEquals("movie3.mp4", movie3.getVideo());
        assertEquals("tag1", movie3.getExtra());
    }

    @Test
    void testFindByIdShouldReturnFirstMovie() {
        Optional<Movie> result = repository.findById(movieId);

        assertTrue(result.isPresent());

        Movie movie = result.get();

        assertEquals(movieId, movie.getId());
        assertEquals("Movie 1", movie.getTitle());
        assertEquals("First movie", movie.getDescription());
        assertEquals("http://movies.info/1.png", movie.getImage());
        assertEquals("movie1.mp4", movie.getVideo());
        assertEquals("tag1,tag2", movie.getExtra());
    }

    @Test
    void testSaveWithNullIdShouldCreateANewMovie() {
        Movie input = new Movie(
                null,
                "Movie 4",
                "Fourth movie",
                "http://movies.info/4.png",
                "movie4.mp4",
                "tag1,tag2,tag3"
        );

        Movie movie = repository.save(input);
        long total = repository.count();

        assertNotNull(movie.getId());
        assertEquals("Movie 4", movie.getTitle());
        assertEquals("Fourth movie", movie.getDescription());
        assertEquals("http://movies.info/4.png", movie.getImage());
        assertEquals("movie4.mp4", movie.getVideo());
        assertEquals("tag1,tag2,tag3", movie.getExtra());

        assertEquals(4L, total);

        List<Movie> allMovies = mongoTemplate.findAll(Movie.class);

        assertEquals(movie, allMovies.get(3));
    }

    @Test
    void saveMovieWithFirstMovieIdShouldUpdateIt() {
        Movie input = new Movie(
                movieId,
                "Movie 1",
                "My first movie",
                "http://movies.info/1.png",
                "movie1.mp4",
                "tag1,tag2,tag3,tag4"
        );

        Movie movie = repository.save(input);
        long total = repository.count();

        assertNotNull(movie);
        assertEquals(movieId, movie.getId());
        assertEquals("Movie 1", movie.getTitle());
        assertEquals("My first movie", movie.getDescription());
        assertEquals("http://movies.info/1.png", movie.getImage());
        assertEquals("movie1.mp4", movie.getVideo());
        assertEquals("tag1,tag2,tag3,tag4", movie.getExtra());

        assertEquals(3L, total);
    }

    @Test
    void deleteByIdShouldRemoveFirstMovie() {
        repository.deleteById(movieId);

        Movie result = mongoTemplate.findById(movieId, Movie.class);
        long total = repository.count();

        assertNull(result);
        assertEquals(2L, total);
    }
}
