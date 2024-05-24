package net.jazbelt.jazflixboapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.jazbelt.jazflixboapi.error.NotImplementedException;
import net.jazbelt.jazflixboapi.model.entity.Movie;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@Tag(name = "Movies", description = "CRUD operations for movies")
public class MovieController {

    @GetMapping
    @Operation(summary = "Get all movies", description = "Return full list of movies")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Return list of movies")
    })
    public List<Movie> getMovies() {
        throw new NotImplementedException();
    }

    @GetMapping("{id}")
    @Operation(summary = "Get single movie", description = "Get single movie by given ID ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Return specific movie data"),
            @ApiResponse(responseCode = "404", description = "Movie not found")
    })
    public Movie getMovie(
            @PathVariable("id")
            @Parameter(required = true, description = "Movie unique ID")
            String id
    ) {
        throw new NotImplementedException();
    }

    @PostMapping
    @Operation(summary = "Create new movie", description = "Post new movie with given info")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "New movie successfully created"),
            @ApiResponse(responseCode = "400", description = "Validation bad request error")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public Movie postNewMovie(@Valid @RequestBody Movie movie) {
        throw new NotImplementedException();
    }

    @PutMapping("{id}")
    @Operation(summary = "Update movie", description = "Send information to update existing movie")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Movie successfully updated"),
            @ApiResponse(responseCode = "400", description = "Validation bad request error"),
            @ApiResponse(responseCode = "404", description = "Movie not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void putMovie(@PathVariable("id") String id, @Valid @RequestBody Movie movie) {
        throw new NotImplementedException();
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete movie", description = "Remove movie by given id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Movie successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Movie not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(String id) {
        throw new NotImplementedException();
    }
}
