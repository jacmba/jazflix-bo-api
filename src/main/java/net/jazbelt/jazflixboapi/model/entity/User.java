package net.jazbelt.jazflixboapi.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "users")
@Schema(description = "Jazflix user object")
public class User {

    @Id
    @Schema(description = "Database unique ID", example = "abc123456")
    private String id;

    @NotBlank
    @NotNull
    @Email
    @Schema(description = "User name (OAuth2 Google email)", example = "john.doe@gmail.com")
    private String name;

    @NotNull
    @Schema(description = "Flag to allow user auth", example = "true")
    private Boolean enabled;
}
