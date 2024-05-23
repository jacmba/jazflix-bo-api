package net.jazbelt.jazflixboapi.integration.repository;

import net.jazbelt.jazflixboapi.model.entity.Section;
import net.jazbelt.jazflixboapi.model.repository.SectionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataMongoTest()
public class SectionRepositoryTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    SectionRepository repository;

    private String sectionId;

    @BeforeEach
    void setUp() {
        Section home = new Section(null, "mdi-home", "Home", "/", 1);
        Section movies = new Section(null, "mdi-movie", "Movies", "/sections/movies", 2);
        Section series = new Section(null, "mdi-series", "Series", "/sections/series", 3);

        sectionId = mongoTemplate.save(home).getId();
        mongoTemplate.save(movies);
        mongoTemplate.save(series);
    }

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection(Section.class);
    }

    @Test
    void findAllShouldReturnThe3Sections() {
        List<Section> sections = repository.findAll();

        assertNotNull(sections);
        assertEquals(3, sections.size());

        Section home = sections.get(0);
        assertNotNull(home.getId());
        assertEquals("mdi-home", home.getIcon());
        assertEquals("Home", home.getTitle());
        assertEquals("/", home.getTo());
        assertEquals(1, home.getOrder());

        Section movies = sections.get(1);
        assertNotNull(movies.getId());
        assertEquals("mdi-movie", movies.getIcon());
        assertEquals("Movies", movies.getTitle());
        assertEquals("/sections/movies", movies.getTo());
        assertEquals(2, movies.getOrder());

        Section series = sections.get(2);
        assertNotNull(series.getId());
        assertEquals("mdi-series", series.getIcon());
        assertEquals("Series", series.getTitle());
        assertEquals("/sections/series", series.getTo());
        assertEquals(3, series.getOrder());
    }

    @Test
    void findByIdShouldReturnHomeSection() {
        Optional<Section> result = repository.findById(sectionId);

        assertTrue(result.isPresent());

        Section section = result.get();

        assertEquals(sectionId, section.getId());
        assertEquals("mdi-home", section.getIcon());
        assertEquals("Home", section.getTitle());
        assertEquals("/", section.getTo());
        assertEquals(1, section.getOrder());
    }

    @Test
    void createNewSectionShouldInsertNewDocument() {
        Section section = new Section(null, "mdi-test", "Test", "/test", 999);

        Section savedSection = repository.save(section);

        assertNotNull(savedSection);
        assertNotNull(savedSection.getId());
        assertEquals("mdi-test", savedSection.getIcon());
        assertEquals("Test", savedSection.getTitle());
        assertEquals("/test", savedSection.getTo());
        assertEquals(999, savedSection.getOrder());

        List<Section> sections = mongoTemplate.findAll(Section.class);

        assertNotNull(sections);
        assertEquals(4, sections.size());

        Section result = sections.get(3);

        assertEquals(savedSection, result);
    }

    @Test
    void updateSectionShouldReflectChanges() {
        Section section = new Section(sectionId, "mdi-test", "Test", "/", 0);

        Section updatedSection = repository.save(section);

        assertEquals(section, updatedSection);

        List<Section> sections = mongoTemplate.findAll(Section.class);

        assertEquals(3, sections.size());

        Section result = sections.get(0);

        assertEquals(updatedSection, result);
    }

    @Test
    void deleteSectionShouldRemoveDocument() {
        repository.deleteById(sectionId);

        List<Section> sections = mongoTemplate.findAll(Section.class);

        assertEquals(2, sections.size());

        Section result = mongoTemplate.findById(sectionId, Section.class);

        assertNull(result);
    }
}
