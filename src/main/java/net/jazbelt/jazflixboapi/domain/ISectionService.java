package net.jazbelt.jazflixboapi.domain;

import net.jazbelt.jazflixboapi.model.entity.Section;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ISectionService {

    Section createSection(Section section);

    List<Section> retrieveAllSections();

    Section retrieveSection(String id);

    void updateSection(String id, Section section);

    void deleteSection(String id);
}
