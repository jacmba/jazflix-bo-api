package net.jazbelt.jazflixboapi.domain;

import net.jazbelt.jazflixboapi.model.entity.User;
import net.jazbelt.jazflixboapi.model.repository.UserRepository;
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
class UserServiceTest {

    IUserService service;

    @BeforeEach
    void setUp(@Mock UserRepository repository) {
        service = new UserService(repository);

        lenient().when(repository.findAll()).thenReturn(
                Arrays.asList(
                        new User("abc123", "jdoe@foo.bar", true),
                        new User("xyz456", "jackie@foo.bar", false)
                )
        );

        lenient().when(repository.findById(anyString())).thenReturn(Optional.empty());
        lenient().when(repository.findById("abc123")).thenReturn(Optional.of(new User("abc123", "jdoe@foo.bar", true)));
        lenient().when(repository.findById("xyz456")).thenReturn(Optional.of(new User("xyz456", "jackie@foo.bar", false)));
    }

    @Test
    void retrieveUserDetails() {
        Optional<User> possibleUser = service.retrieveUserDetails("abc123");
        assertTrue(possibleUser.isPresent());

        User user = possibleUser.get();

        assertEquals("abc123", user.getId());
        assertEquals("jdoe@foo.bar", user.getName());
        assertTrue(user.getEnabled());
    }

    @Test
    void retrieveAllUsers() {
        List<User> users = service.retrieveAllUsers();

        assertEquals(2, users.size());

        User user = users.get(0);
        assertEquals("abc123", user.getId());
        assertEquals("jdoe@foo.bar", user.getName());
        assertTrue(user.getEnabled());

        user = users.get(1);
        assertEquals("xyz456", user.getId());
        assertEquals("jackie@foo.bar", user.getName());
        assertFalse(user.getEnabled());
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }
}