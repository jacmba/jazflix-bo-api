package net.jazbelt.jazflixboapi;

import net.jazbelt.jazflixboapi.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.awt.desktop.SystemEventListener;

@Component
public class TestAppRunner implements CommandLineRunner {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) throws Exception {
        // Cleanup collections
        mongoTemplate.dropCollection("users");

        // Populate users
        mongoTemplate.save(new User(null, "jdoe@foo.bar", true));
        mongoTemplate.save(new User(null, "jane@foo.bar", true));
        mongoTemplate.save(new User(null, "jack@foo.bar", false));
    }
}
