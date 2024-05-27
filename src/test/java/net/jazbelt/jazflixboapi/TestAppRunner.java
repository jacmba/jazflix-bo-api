package net.jazbelt.jazflixboapi;

import net.jazbelt.jazflixboapi.model.entity.Movie;
import net.jazbelt.jazflixboapi.model.entity.Section;
import net.jazbelt.jazflixboapi.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class TestAppRunner implements CommandLineRunner {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) throws Exception {
        // Cleanup collections
        mongoTemplate.dropCollection("users");
        mongoTemplate.dropCollection("sections");
        mongoTemplate.dropCollection("movies");

        // Populate users
        mongoTemplate.save(new User(null, "jdoe@foo.bar", true));
        mongoTemplate.save(new User(null, "jane@foo.bar", true));
        mongoTemplate.save(new User(null, "jack@foo.bar", false));

        // Populate sections
        mongoTemplate.save(new Section(null, "icon-home", "Home", "/", 1));
        mongoTemplate.save(new Section(null, "icon-movies", "Movies", "/movies", 2));
        mongoTemplate.save(new Section(null, "icon-series", "Series", "/series", 3));

        // Populate movies
        mongoTemplate.save(new Movie(null, "Movie 1", "first", "1.png", "1.mp4", "tag1"));
        mongoTemplate.save(new Movie(null, "Movie 2", "second", "2.png", "2.mp4", "tag1"));
        mongoTemplate.save(new Movie(null, "Movie 3", "third", "3.png", "3.mp4", "tag1"));
    }
}
