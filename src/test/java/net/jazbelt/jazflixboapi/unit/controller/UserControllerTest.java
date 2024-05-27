package net.jazbelt.jazflixboapi.unit.controller;

import net.jazbelt.jazflixboapi.controller.UserController;
import net.jazbelt.jazflixboapi.domain.UserService;
import net.jazbelt.jazflixboapi.model.entity.User;
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
class UserControllerTest {

    UserController controller;

    @Mock
    UserService userService;

    @BeforeEach
    void setUp() {
        controller = new UserController(userService);

        User john = new User("abc123", "jdoe@foo.bar", true);
        User jack = new User("xyz456", "jack@foo.bar", false);
        User foo = new User("aabbcc", "foo@bar", true);

        lenient().when(userService.retrieveAllUsers()).thenReturn(Arrays.asList(john, jack));
        lenient().when(userService.retrieveUserDetails("abc123")).thenReturn(john);
        lenient().when(userService.retrieveUserDetails("xyz456")).thenReturn(jack);
        lenient().when(userService.createUser(any(User.class))).thenReturn(foo);
    }

    @Test
    void getUsers() {
        List<User> users = controller.getUsers();

        verify(userService).retrieveAllUsers();

        assertEquals(2, users.size());

        User john = users.get(0);
        assertEquals("abc123", john.getId());
        assertEquals("jdoe@foo.bar", john.getName());
        assertTrue(john.getEnabled());

        User jack = users.get(1);
        assertEquals("xyz456", jack.getId());
        assertEquals("jack@foo.bar", jack.getName());
        assertFalse(jack.getEnabled());
    }

    @Test
    void getUser() {
        User user = controller.getUser("abc123");

        verify(userService).retrieveUserDetails("abc123");

        assertEquals("abc123", user.getId());
        assertEquals("jdoe@foo.bar", user.getName());
        assertTrue(user.getEnabled());
    }

    @Test
    void postCreateUser() {
        User input = new User(null, "foo@bar", true);
        User user = controller.postCreateUser(input);

        verify(userService).createUser(input);

        assertEquals("aabbcc", user.getId());
        assertEquals("foo@bar", user.getName());
        assertTrue(user.getEnabled());
    }

    @Test
    void putUpdateUser() {
        User input = new User("aabbcc", "foo@bar", true);
        controller.putUpdateUser("aabbcc", input);

        verify(userService).updateUser("aabbcc", input);
    }

    @Test
    void deleteUser() {
        controller.deleteUser("abc123");
        verify(userService).deleteUser("abc123");
    }
}