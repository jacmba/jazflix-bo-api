package net.jazbelt.jazflixboapi.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Document(collection = "sections")
public class Section {

    @Id
    @Schema(description = "Section DB unique ID", example = "abc123456")
    private String id;

    @NotNull
    @NotEmpty
    @Schema(description = "Icon reference", example = "my-icon")
    private String icon;

    @NotNull
    @NotEmpty
    @Schema(description = "Section title", example = "My Section")
    private String title;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^\\/.*")
    @Schema(description = "Navigation URL", example = "/sections/my_section")
    private String to;

    @Schema(description = "Display order", example = "1")
    private Integer order = 0;
}
