package net.jazbelt.jazflixboapi.controller;

import jakarta.validation.Valid;
import net.jazbelt.jazflixboapi.error.NotImplementedException;
import net.jazbelt.jazflixboapi.model.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping
    public List<User> getUsers() {
        throw new NotImplementedException();
    }

    @GetMapping("{id}")
    public User getUser(@PathVariable("id") String id) {
        throw new NotImplementedException();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User postCreateUser(@RequestBody @Valid User user) {
        throw new NotImplementedException();
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public User putUpdateUser(@PathVariable("id") String id, @RequestBody @Valid User user) {
        throw new NotImplementedException();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") String id) {
        throw new NotImplementedException();
    }
}
