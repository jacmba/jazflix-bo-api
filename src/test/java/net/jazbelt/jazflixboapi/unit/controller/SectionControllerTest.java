package net.jazbelt.jazflixboapi.unit.controller;

import net.jazbelt.jazflixboapi.controller.SectionController;
import net.jazbelt.jazflixboapi.domain.SectionService;
import net.jazbelt.jazflixboapi.model.entity.Section;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SectionControllerTest {

    SectionController controller;

    @Mock
    private SectionService service;

    @BeforeEach
    void setUp() {
        controller = new SectionController(service);

        Section home = new Section("1", "icon-home", "Home", "/", 1);
        Section movies = new Section("2", "icon-movies", "Movies", "/movies", 2);
        Section series = new Section("3", "icon-series", "Series", "/series", 3);

        lenient().when(service.createSection(any(Section.class))).thenReturn(home);
        lenient().when(service.retrieveAllSections()).thenReturn(Arrays.asList(home, movies, series));
        lenient().when(service.retrieveSection(anyString())).thenReturn(home);
        lenient().when(service.updateSection(anyString(), any(Section.class))).thenReturn(home);
    }

    @Test
    void getAllSectionsShouldReturnAllSections() {
        List<Section> sections = controller.getAllSections();

        verify(service).retrieveAllSections();

        assertNotNull(sections);
        assertEquals(3, sections.size());

        Section home = sections.get(0);
        assertEquals("1", home.getId());
        assertEquals("icon-home", home.getIcon());
        assertEquals("Home", home.getTitle());
        assertEquals("/", home.getTo());
        assertEquals(1, home.getOrder());

        Section movies = sections.get(1);
        assertEquals("2", movies.getId());
        assertEquals("icon-movies", movies.getIcon());
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
    public void getSingleSectionShouldReturnHomeSection() {
        Section section = controller.getSection("1");

        verify(service).retrieveSection("1");

        assertNotNull(section);
        assertEquals("1", section.getId());
        assertEquals("icon-home", section.getIcon());
        assertEquals("Home", section.getTitle());
        assertEquals("/", section.getTo());
        assertEquals(1, section.getOrder());
    }

    @Test
    public void postNewSectionShouldReturnNewCreatedSection() {
        Section input = new Section(null, "icon-home", "Home", "/", 1);

        Section section = controller.postNewSection(input);

        verify(service).createSection(input);

        assertNotNull(section);
        assertEquals("1", section.getId());
        assertEquals("icon-home", section.getIcon());
        assertEquals("Home", section.getTitle());
        assertEquals("/", section.getTo());
        assertEquals(1, section.getOrder());
    }

    @Test
    public void putUpdateSectionShouldReturnChangedSection() {
        Section input = new Section("1", "icon-home", "Home", "/", 1);

        Section section = controller.putUpdateSection("1", input);

        verify(service).updateSection("1", input);

        assertNotNull(section);
        assertEquals("1", section.getId());
        assertEquals("icon-home", section.getIcon());
        assertEquals("Home", section.getTitle());
        assertEquals("/", section.getTo());
        assertEquals(1, section.getOrder());
    }

    @Test
    public void deleteSectionShouldInvokeServiceDeleteMethod() {
        controller.deleteSection("123");
        verify(service).deleteSection("123");
    }
}
