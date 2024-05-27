package net.jazbelt.jazflixboapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.jazbelt.jazflixboapi.domain.ISectionService;
import net.jazbelt.jazflixboapi.model.entity.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/section")
@Tag(name = "Sections", description = "CRUD operations for movie sections")
public class SectionController {

    private final ISectionService service;

    @Autowired
    public SectionController(ISectionService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Get all sections", description = "Retrieve full list of sections")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Return list of sections")
    })
    public List<Section> getAllSections() {
        return service.retrieveAllSections();
    }

    @GetMapping("{id}")
    @Operation(summary = "Get single section", description = "Retrieve specific section by given ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Return section object"),
            @ApiResponse(responseCode = "404", description = "Section not found")
    })
    public Section getSection(@PathVariable("id") String id) {
        return service.retrieveSection(id);
    }

    @PostMapping
    @Operation(summary = "Create new section", description = "Post new section info")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "New section successfully created"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public Section postNewSection(@Valid @RequestBody Section section) {
        return service.createSection(section);
    }

    @PutMapping("{id}")
    @Operation(summary = "Update section", description = "Modify existing section by given ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Section successfully updated"),
            @ApiResponse(responseCode = "409", description = "Path and object IDs do not match"),
            @ApiResponse(responseCode = "404", description = "Section not found")
    })
    public void putUpdateSection(@PathVariable("id") String id, @Valid @RequestBody Section section) {
        service.updateSection(id, section);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete section", description = "Remove section by given ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Section successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Section not found")
    })
    public void deleteSection(@PathVariable("id") String id) {
        service.deleteSection(id);
    }
}
