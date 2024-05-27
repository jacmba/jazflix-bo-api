package net.jazbelt.jazflixboapi.e2e;

import net.jazbelt.jazflixboapi.model.entity.Section;
import net.jazbelt.jazflixboapi.model.repository.SectionRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class SectionE2ETests {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private RestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Autowired
    private SectionRepository sectionRepository;

    private String baseUri;

    private static String sectionId;

    @BeforeEach
    void setUp() {
        baseUri = String.format("http://localhost:%d/section", port);
    }

    @Test
    @Order(1)
    void getSectionsShouldReturnAllSections() {
        Section[] result = restTemplate.getForObject(baseUri, Section[].class);

        assertNotNull(result);
        assertEquals(3, result.length);

        assertNotNull(result[0].getId());
        assertEquals("icon-home", result[0].getIcon());
        assertEquals("Home", result[0].getTitle());
        assertEquals("/", result[0].getTo());
        assertEquals(1, result[0].getOrder());

        assertNotNull(result[1].getId());
        assertEquals("icon-movies", result[1].getIcon());
        assertEquals("Movies", result[1].getTitle());
        assertEquals("/movies", result[1].getTo());
        assertEquals(2, result[1].getOrder());

        assertNotNull(result[2].getId());
        assertEquals("icon-series", result[2].getIcon());
        assertEquals("Series", result[2].getTitle());
        assertEquals("/series", result[2].getTo());
        assertEquals(3, result[2].getOrder());

        sectionId = result[0].getId();
    }

    @Test
    @Order(2)
    void getSingleSectionShouldReturnHomeSection() {
        Section section = restTemplate.getForObject(baseUri + "/" + sectionId, Section.class);

        assertNotNull(section);
        assertEquals(sectionId, section.getId());
        assertEquals("icon-home", section.getIcon());
        assertEquals("Home", section.getTitle());
        assertEquals("/", section.getTo());
        assertEquals(1, section.getOrder());
    }

    @Test
    @Order(3)
    void getNonExistingSectionSouldReturnNotFoundError() {
        HttpClientErrorException.NotFound ex = assertThrows(HttpClientErrorException.NotFound.class, () ->
            restTemplate.getForObject(baseUri + "/xxyyzz", Section.class)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    @Order(4)
    void postNewSectionShouldCreateNewRecord() {
        Section input = new Section(null, "test", "Test", "/test", 4);

        Section newSection = restTemplate.postForObject(baseUri, input, Section.class);

        assertNotNull(newSection);
        assertNotNull(newSection.getId());
        assertEquals("test", newSection.getIcon());
        assertEquals("Test", newSection.getTitle());
        assertEquals("/test", newSection.getTo());
        assertEquals(4, newSection.getOrder());

        sectionId = newSection.getId();

        Optional<Section> result = sectionRepository.findById(sectionId);
        long total = sectionRepository.count();

        assertTrue(result.isPresent());
        assertEquals(newSection, result.get());
        assertEquals(4L, total);
    }

    @Test
    @Order(5)
    void postNewSectionWithNullIconShouldReturnBadRequestError() {
        HttpClientErrorException.BadRequest ex = assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            Section input = new Section(null, null, "Test", "/test", 999);
            restTemplate.postForObject(baseUri, input, Section.class);
        });

        long total = sectionRepository.count();

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals(4L, total);
    }

    @Test
    @Order(6)
    void postNewSectionWithNullTitleShouldReturnBadRequestError() {
        HttpClientErrorException.BadRequest ex = assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            Section input = new Section(null, "icon", null, "/test", 999);
            restTemplate.postForObject(baseUri, input, Section.class);
        });

        long total = sectionRepository.count();

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals(4L, total);
    }

    @Test
    @Order(7)
    void postNewSectionWithNullToShouldReturnBadRequestError() {
        HttpClientErrorException.BadRequest ex = assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            Section input = new Section(null, "icon", "Test", null, 999);
            restTemplate.postForObject(baseUri, input, Section.class);
        });

        long total = sectionRepository.count();

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals(4L, total);
    }

    @Test
    @Order(7)
    void postNewSectionWithInvalidToShouldReturnBadRequestError() {
        HttpClientErrorException.BadRequest ex = assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            Section input = new Section(null, "icon", "Test", "test", 999);
            restTemplate.postForObject(baseUri, input, Section.class);
        });

        long total = sectionRepository.count();

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals(4L, total);
    }

    @Test
    @Order(10)
    void putUpdateSectionShouldReflectChanges() {
        Section input = new Section(sectionId, "test-icon", "Test 2", "/test", 999);

        restTemplate.put(baseUri + "/" + sectionId, input);

        Optional<Section> result = sectionRepository.findById(sectionId);
        long total = sectionRepository.count();

        assertTrue(result.isPresent());
        assertEquals(input, result.get());
        assertEquals(4L, total);
    }

    @Test
    @Order(12)
    void putUpdateNonExistingSectionShouldReturnNotFoundError() {
        HttpClientErrorException.NotFound ex = assertThrows(HttpClientErrorException.NotFound.class, () -> {
            Section input = new Section("123456", "test", "Test", "/test", 999);
            restTemplate.put(baseUri + "/123456", input);
        });

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    @Order(13)
    void putUpdateSectionWithNullIconShouldReturnBadRequestError() {
        HttpClientErrorException.BadRequest ex = assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            Section input = new Section("123456", null, "Test", "/test", 999);
            restTemplate.put(baseUri + "/123456", input);
        });

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    @Order(14)
    void putUpdateSectionWithNullTitleShouldReturnBadRequestError() {
        HttpClientErrorException.BadRequest ex = assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            Section input = new Section("123456", "test", null, "/test", 999);
            restTemplate.put(baseUri + "/123456", input);
        });

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    @Order(15)
    void putUpdateSectionWithNullToShouldReturnBadRequestError() {
        HttpClientErrorException.BadRequest ex = assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            Section input = new Section("123456", "test", "Test", null, 999);
            restTemplate.put(baseUri + "/123456", input);
        });

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    @Order(15)
    void putUpdateSectionWithInvalidToShouldReturnBadRequestError() {
        HttpClientErrorException.BadRequest ex = assertThrows(HttpClientErrorException.BadRequest.class, () -> {
            Section input = new Section("123456", "test", "Test", "test", 999);
            restTemplate.put(baseUri + "/123456", input);
        });

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    @Order(20)
    void deleteSectionShouldRemoveRecord() {
        restTemplate.delete(baseUri + "/" + sectionId);

        Optional<Section> result = sectionRepository.findById(sectionId);
        long total = sectionRepository.count();

        assertTrue(result.isEmpty());
        assertEquals(3L, total);
    }

    @Test
    @Order(21)
    void deleteNonExistingSectionShouldReturnNotFoundError() {
        HttpClientErrorException.NotFound ex = assertThrows(HttpClientErrorException.NotFound.class, () ->
            restTemplate.delete(baseUri + "/12341123asdlfkjads")
        );

        long total = sectionRepository.count();

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals(3L, total);
    }
}
