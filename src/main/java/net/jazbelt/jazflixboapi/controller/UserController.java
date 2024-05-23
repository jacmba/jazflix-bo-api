package net.jazbelt.jazflixboapi.controller;

import jakarta.validation.Valid;
import net.jazbelt.jazflixboapi.domain.IUserService;
import net.jazbelt.jazflixboapi.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final IUserService service;

    @Autowired
    public UserController(IUserService service) {
        this.service = service;
    }

    @GetMapping
    public List<User> getUsers() {
        return service.retrieveAllUsers();
    }

    @GetMapping("{id}")
    public User getUser(@PathVariable("id") String id) {
        return service.retrieveUserDetails(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User postCreateUser(@RequestBody @Valid User user) {
        return service.createUser(user);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User putUpdateUser(@PathVariable("id") String id, @Valid @RequestBody User user) {
        return service.updateUser(id, user);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") String id) {
        service.deleteUser(id);
    }
}
