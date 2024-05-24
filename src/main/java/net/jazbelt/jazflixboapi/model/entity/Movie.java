package net.jazbelt.jazflixboapi.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
@Document(collection = "movies")
@Schema(description = "Movies data object")
public class Movie {

    @Id
    @Schema(description = "Database unique ID", example = "abc123")
    private String id;

    @NotNull
    @NotEmpty
    @Min(value = 5, message = "Title should have at least 5 characters")
    @Schema(description = "Movie title", example = "My awesome movie")
    private String title;

    @Schema(description = "Movie description", example = "A funny and exciting film")
    private String description;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^(https?:\\\\/\\\\/)?([\\\\da-z\\\\.-]+)\\\\.([a-z\\\\.]{2,6})([\\\\/\\w \\\\.-]*)*\\\\/?$", message = "Invalid URL format")
    @Schema(description = "Image source in URL format", example = "http://myawesomemovie.info/image.png")
    private String image;

    @NotNull
    @NotEmpty
    @Min(value = 5, message = "Video file name should have at least 5 characters")
    @Schema(description = "Video file name")
    private String video;

    @Schema(description = "Extra information comma-separated tags for grouping and sorting", example = "movies,cartoon")
    private String extra;
}
