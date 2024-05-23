package net.jazbelt.jazflixboapi.model.entity;

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
@Document(collection = "sections")
public class Section {

    @Id
    private String id;

    @NotNull
    private String icon;

    @NotNull
    private String title;

    @NotNull
    private String to;

    private Integer order = 0;
}
