package net.jazbelt.jazflixboapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.jazbelt.jazflixboapi.domain.IUserService;
import net.jazbelt.jazflixboapi.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "Users", description = "CRUD operations for users")
public class UserController {

    private final IUserService service;

    @Autowired
    public UserController(IUserService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve full list of users")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Return list of users")
    })
    public List<User> getUsers() {
        return service.retrieveAllUsers();
    }

    @GetMapping("{id}")
    @Operation(summary = "Get single user", description = "Retrieve specific user by given ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Return single user"),
            @ApiResponse(responseCode = "400", description = "User not found")
    })
    public User getUser(@PathVariable("id") String id) {
        return service.retrieveUserDetails(id);
    }

    @PostMapping
    @Operation(summary = "Create new user", description = "Post new user with given info")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public User postCreateUser(@RequestBody @Valid User user) {
        return service.createUser(user);
    }

    @PutMapping("{id}")
    @Operation(summary = "Update user", description = "Put modified user info")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User successfully updated"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "Path and object user IDs do not match")
    })
    public void putUpdateUser(@PathVariable("id") String id, @Valid @RequestBody User user) {
        service.updateUser(id, user);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete user", description = "Remove user by given ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User successfully deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public void deleteUser(@PathVariable("id") String id) {
        service.deleteUser(id);
    }
}
