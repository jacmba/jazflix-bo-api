package net.jazbelt.jazflixboapi.domain;

import net.jazbelt.jazflixboapi.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IUserService {
    User retrieveUserDetails(String id);

    List<User> retrieveAllUsers();

    User createUser(User user);

    User updateUser(String id, User user);

    void deleteUser(String id);
}
