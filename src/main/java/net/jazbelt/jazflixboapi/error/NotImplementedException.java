package net.jazbelt.jazflixboapi.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_IMPLEMENTED, reason = "Working on it!")
public class NotImplementedException extends RuntimeException {
}
