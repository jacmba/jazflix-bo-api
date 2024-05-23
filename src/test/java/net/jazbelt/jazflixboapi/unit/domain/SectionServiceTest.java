package net.jazbelt.jazflixboapi.unit.domain;

import net.jazbelt.jazflixboapi.domain.ISectionService;
import net.jazbelt.jazflixboapi.domain.SectionService;
import net.jazbelt.jazflixboapi.error.SectionIdMismatchException;
import net.jazbelt.jazflixboapi.error.SectionNotFoundException;
import net.jazbelt.jazflixboapi.model.entity.Section;
import net.jazbelt.jazflixboapi.model.repository.SectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SectionServiceTest {

    private ISectionService service;

    @Mock
    private SectionRepository repository;

    @BeforeEach
    void setup() {
        service = new SectionService(repository);

        Section home = new Section("1", "icon-home", "Home", "/", 1);
        Section movies = new Section("2", "icon-movie", "Movies", "/movies", 2);
        Section series = new Section("3", "icon-series", "Series", "/series", 3);

        lenient().when(repository.findAll()).thenReturn(Arrays.asList(home, movies, series));
        lenient().when(repository.findById("1")).thenReturn(Optional.of(home));
        lenient().when(repository.findById("x")).thenReturn(Optional.empty());
        lenient().when(repository.save(any(Section.class))).thenReturn(home);
    }

    @Test
    void sectionServiceShouldNotBeNull() {
        assertNotNull(service);
    }

    @Test
    void retrieveAllSectionsSouldReturnListWith3Elements() {
        List<Section> sections = service.retrieveAllSections();

        verify(repository).findAll();
        assertEquals(3, sections.size());

        Section home = sections.get(0);
        assertEquals("1", home.getId());
        assertEquals("icon-home", home.getIcon());
        assertEquals("Home", home.getTitle());
        assertEquals("/", home.getTo());
        assertEquals(1, home.getOrder());

        Section movies = sections.get(1);
        assertEquals("2", movies.getId());
        assertEquals("icon-movie", movies.getIcon());
        assertEquals("Movies", movies.getTitle());
        assertEquals("/movies", movies.getTo());
        assertEquals(2, movies.getOrder());

        Section series = sections.get(2);
        assertEquals("3", series.getId());
        assertEquals("icon-series", series.getIcon());
        assertEquals("Series", series.getTitle());
        assertEquals("/series", series.getTo());
        assertEquals(3, series.getOrder());
    }

    @Test
    void retrieveSectionShouldReturnExistingDocument() {
        Section section = service.retrieveSection("1");

        verify(repository).findById("1");

        assertEquals("1", section.getId());
        assertEquals("icon-home", section.getIcon());
        assertEquals("Home", section.getTitle());
        assertEquals("/", section.getTo());
        assertEquals(1, section.getOrder());
    }

    @Test
    void retrieveNonExistingSectionShouldThrowNotFoundException() {
        SectionNotFoundException ex = assertThrows(SectionNotFoundException.class, () -> {
            service.retrieveSection("x");
        });

        assertEquals("Section [x] not found", ex.getMessage());
    }

    @Test
    void createSectionShouldReturnHomeDocument() {
        Section section = new Section(null, "icon-home", "Home", "/", 1);
        Section createdSection = service.createSection(section);

        verify(repository).save(section);

        assertEquals("1", createdSection.getId());
        assertEquals(section.getIcon(), createdSection.getIcon());
        assertEquals(section.getTitle(), createdSection.getTitle());
        assertEquals(section.getTo(), createdSection.getTo());
        assertEquals(section.getOrder(), createdSection.getOrder());
    }

    @Test
    void updateSectionShouldReturnModifiedDocument() {
        Section section = new Section("1", "icon-home", "Home", "/", 1);
        Section updatedSection = service.updateSection("1", section);

        verify(repository).save(section);
        assertEquals(section, updatedSection);
    }

    @Test
    void updateSectionShouldThrowIDMismatchException() {
        SectionIdMismatchException ex = assertThrows(SectionIdMismatchException.class, () -> {
            service.updateSection("2", new Section("1", "test", "Test", "/test", 1));
        });

        assertEquals("Section IDs do not match", ex.getMessage());
    }

    @Test
    void updateNonExistingSectionShouldThrowNotFoundException() {
        SectionNotFoundException ex = assertThrows(SectionNotFoundException.class, () -> {
            service.updateSection("x", new Section("x", "test", "Test", "/test", 1));
        });

        assertEquals("Section [x] not found", ex.getMessage());
    }

    @Test
    void deleteSectionShouldInvokeRepositoryMethod() {
        service.deleteSection("1");

        verify(repository).deleteById("1");
    }

    @Test
    void deleteNonExistingSectionShouldThrowNotFoundException() {
        SectionNotFoundException ex = assertThrows(SectionNotFoundException.class, () -> {
            service.deleteSection("x");
        });

        assertEquals("Section [x] not found", ex.getMessage());
    }
}
