package net.jazbelt.jazflixboapi.domain;

import net.jazbelt.jazflixboapi.model.entity.User;
import net.jazbelt.jazflixboapi.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<User> retrieveUserDetails(String id) {
        return repository.findById(id);
    }

    @Override
    public List<User> retrieveAllUsers() {
        return repository.findAll();
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
