package net.jazbelt.jazflixboapi.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Sections ID mismatch")
public class SectionIdMismatchException extends RuntimeException {

    public SectionIdMismatchException() {
        super("Section IDs do not match");
    }
}
