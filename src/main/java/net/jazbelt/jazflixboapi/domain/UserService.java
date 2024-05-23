package net.jazbelt.jazflixboapi.domain;

import net.jazbelt.jazflixboapi.error.UserIdMismatchException;
import net.jazbelt.jazflixboapi.error.UserNotFoundException;
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
    public User retrieveUserDetails(String id) {
        Optional<User> user = repository.findById(id);

        if (user.isEmpty()) {
            throw new UserNotFoundException(id);
        }

        return user.get();
    }

    @Override
    public List<User> retrieveAllUsers() {
        return repository.findAll();
    }

    @Override
    public User createUser(User user) {
        return repository.save(user);
    }

    @Override
    public User updateUser(String id, User user) {
        if (!id.equals(user.getId())) {
            throw new UserIdMismatchException();
        }

        retrieveUserDetails(id);

        return repository.save(user);
    }

    @Override
    public void deleteUser(String id) {
        retrieveUserDetails(id);
        repository.deleteById(id);
    }
}
