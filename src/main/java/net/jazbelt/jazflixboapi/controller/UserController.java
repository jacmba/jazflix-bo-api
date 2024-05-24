package net.jazbelt.jazflixboapi.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Return list of users")
    })
    public List<User> getUsers() {
        return service.retrieveAllUsers();
    }

    @GetMapping("{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Return single user"),
            @ApiResponse(responseCode = "400", description = "User not found")
    })
    public User getUser(@PathVariable("id") String id) {
        return service.retrieveUserDetails(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public User postCreateUser(@RequestBody @Valid User user) {
        return service.createUser(user);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "User successfully updated"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "Path and object user IDs do not match")
    })
    public User putUpdateUser(@PathVariable("id") String id, @Valid @RequestBody User user) {
        return service.updateUser(id, user);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User successfully deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public void deleteUser(@PathVariable("id") String id) {
        service.deleteUser(id);
    }
}
