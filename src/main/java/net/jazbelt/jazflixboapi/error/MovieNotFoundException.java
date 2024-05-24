package net.jazbelt.jazflixboapi.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Movie not found")
public class MovieNotFoundException extends Exception {

    public MovieNotFoundException(String id) {
        super(String.format("Movie [%s] not found", id));
    }
}
