package net.jazbelt.jazflixboapi.domain;

import net.jazbelt.jazflixboapi.model.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Override
    public Optional<User> retrieveUserDetails(String id) {
        return Optional.empty();
    }

    @Override
    public List<User> retrieveAllUsers() {
        return List.of();
    }

    @Override
    public Optional<User> createUser(User user) {
        return Optional.empty();
    }

    @Override
    public Optional<User> updateUser(String id, User user) {
        return Optional.empty();
    }

    @Override
    public void deleteUser(String id) {

    }
}
