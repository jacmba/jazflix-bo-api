package net.jazbelt.jazflixboapi;

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

        // Populate users
        mongoTemplate.save(new User(null, "jdoe@foo.bar", true));
        mongoTemplate.save(new User(null, "jane@foo.bar", true));
        mongoTemplate.save(new User(null, "jack@foo.bar", false));

        // Populate sections
        mongoTemplate.save(new Section(null, "icon-home", "Home", "/", 1));
        mongoTemplate.save(new Section(null, "icon-movies", "Movies", "/movies", 2));
        mongoTemplate.save(new Section(null, "icon-series", "Series", "/series", 3));
    }
}
