package net.jazbelt.jazflixboapi.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Section not found")
public class SectionNotFoundException extends RuntimeException {

    public SectionNotFoundException(String id) {
        super(String.format("Section [%s] not found", id));
    }
}
