package net.jazbelt.jazflixboapi.integration.repository;

import net.jazbelt.jazflixboapi.model.entity.User;
import net.jazbelt.jazflixboapi.model.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDataSourceConfiguration;
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
@DataMongoTest(excludeAutoConfiguration = EmbeddedDataSourceConfiguration.class)
class UserRepositoryTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    UserRepository repository;

    @BeforeEach
    void setUp() {
        User john = new User("abc123", "jdoe@foo.bar", true);
        User jack = new User("abc456", "jackie@foo.bar", false);
        mongoTemplate.save(john);
        mongoTemplate.save(jack);
    }

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection(User.class);
    }

    @Test
    void userCollectionShouldHave2Users() {
        List<User> users = repository.findAll();

        assertEquals(2, users.size());

        User user = users.get(0);
        assertEquals("abc123", user.getId());
        assertEquals("jdoe@foo.bar", user.getName());
        assertTrue(user.getEnabled());

        user = users.get(1);
        assertEquals("abc456", user.getId());
        assertEquals("jackie@foo.bar", user.getName());
        assertFalse(user.getEnabled());
    }

    @Test
    void validUserShouldExist() {
        Optional<User> possibleUser = repository.findById("abc123");
        assertTrue(possibleUser.isPresent());

        User user = possibleUser.get();
        assertEquals("jdoe@foo.bar", user.getName());
        assertTrue(user.getEnabled());
    }

    @Test
    void invalidUserShouldNotExist() {
        Optional<User> user = repository.findById("xyz789");
        assertTrue(user.isEmpty());
    }

    @Test
    void newUserWithNullIdShouldReturnAValidId() {
        User input = new User(null, "jane@foo.bar", true);
        User result = repository.save(input);

        System.out.println("Created user: " + result);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertTrue(result.getId().length() > 1);
        assertEquals("jane@foo.bar", result.getName());
        assertTrue(result.getEnabled());
    }
}