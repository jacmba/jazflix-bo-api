package net.jazbelt.jazflixboapi.e2e;

import net.jazbelt.jazflixboapi.model.entity.User;
import net.jazbelt.jazflixboapi.model.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserE2ETests {

    @Autowired
    private RestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    private String baseUri;

    private static String userId;

    @BeforeEach
    void setUp() {
        baseUri = String.format("http://localhost:%d/user", port);
    }

    @Test
    void portShouldbeHigherThan1000() {
        assertTrue(port > 1000);
    }

    @Test
    @Order(1)
    void getUsersShouldReturnAllUsers() throws Exception {
        User[] users = restTemplate.getForObject(baseUri, User[].class);
        assertNotNull(users);
        assertEquals(3, users.length);

        System.out.println("Users found: " + Arrays.toString(users));

        User john = users[0];
        assertNotNull(john.getId());
        assertEquals("jdoe@foo.bar", john.getName());
        assertTrue(john.getEnabled());

        User jane = users[1];
        assertNotNull(jane.getId());
        assertEquals("jane@foo.bar", jane.getName());
        assertTrue(jane.getEnabled());

        User jack = users[2];
        assertNotNull(jack.getId());
        assertEquals("jack@foo.bar", jack.getName());
        assertFalse(jack.getEnabled());

        userId = john.getId();
    }

    @Test
    @Order(2)
    void getUserShouldReturnJohnDoeDetails() {
        User user = restTemplate.getForObject(baseUri + "/" + userId, User.class);

        assertNotNull(user);
        assertEquals(userId, user.getId());
        assertEquals("jdoe@foo.bar", user.getName());
        assertTrue(user.getEnabled());
    }

    @Test
    @Order(3)
    void createNewUserShouldReturnSuccessfullyCreatedUser() {
        User user = new User(null, "maria@foo.bar", true);

        User result = restTemplate.postForObject(baseUri, user, User.class);

        System.out.println("Created user: " + result);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertTrue(result.getId().length() > 1);
        assertEquals("maria@foo.bar", result.getName());
        assertTrue(result.getEnabled());
    }

    @Test
    @Order(4)
    void updateJohnDoeShouldReflectChanges() {
        User user = new User(userId, "john@foo.bar", false);

        restTemplate.put(baseUri + "/" + userId, user);

        Optional<User> result = userRepository.findById(userId);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
        assertEquals("john@foo.bar", result.get().getName());
        assertFalse(result.get().getEnabled());
    }

    @Test
    @Order(5)
    void deleteJohnUserSouldSuccessfullyDeleteFromDb() {
        restTemplate.delete(baseUri + "/" + userId);

        List<User> allUsers = userRepository.findAll();

        assertEquals(3, allUsers.size());

        Optional<User> result = userRepository.findById(userId);
        assertTrue(result.isEmpty());
    }

    @Test
    void getNotExistingUserShouldThrowNotFoundException() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> {
            restTemplate.getForObject(baseUri + "/xxyyzz", User.class);
        });
    }

    @Test
    void updateUserWithIdMismatchShouldThrowConflictException() {
        assertThrows(HttpClientErrorException.Conflict.class, () -> {
            User user = new User("aabbcc", "test@lorem.ipsum", true);
            restTemplate.put(baseUri + "/abc", user);
        });
    }

    @Test
    void updateNonExistingUserShouldThrowNotFoundException() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> {
            User user = new User("abc", "test@lorem.ipsum", true);
            restTemplate.put(baseUri + "/abc", user);
        });
    }

    @Test
    void deleteNonExistingUserShouldThrowNotFoundException() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> {
            restTemplate.delete(baseUri + "abc");
        });
    }
}
