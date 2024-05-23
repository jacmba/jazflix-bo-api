package net.jazbelt.jazflixboapi.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Path and object user IDs do not match")
public class UserIdMismatchException extends RuntimeException {
    public UserIdMismatchException() {
        super("Path and object user IDs do not match");
    }
}
