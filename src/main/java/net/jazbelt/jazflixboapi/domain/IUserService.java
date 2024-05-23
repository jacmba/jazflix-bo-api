package net.jazbelt.jazflixboapi.domain;

import net.jazbelt.jazflixboapi.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IUserService {
    Optional<User> retrieveUserDetails(String id);

    List<User> retrieveAllUsers();

    Optional<User> createUser(User user);

    Optional<User> updateUser(String id, User user);

    void deleteUser(String id);
}
