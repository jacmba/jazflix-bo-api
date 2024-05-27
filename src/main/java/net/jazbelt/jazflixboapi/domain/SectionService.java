package net.jazbelt.jazflixboapi.domain;

import net.jazbelt.jazflixboapi.error.SectionNotFoundException;
import net.jazbelt.jazflixboapi.model.entity.Section;
import net.jazbelt.jazflixboapi.model.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SectionService implements ISectionService {

    private final SectionRepository repository;

    @Autowired
    public SectionService(SectionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Section createSection(Section section) {
        return repository.save(section);
    }

    @Override
    public List<Section> retrieveAllSections() {
        return repository.findAll();
    }

    @Override
    public Section retrieveSection(String id) {
        Optional<Section> result = repository.findById(id);

        if (result.isEmpty()) {
            throw new SectionNotFoundException(id);
        }

        return result.get();
    }

    @Override
    public void updateSection(String id, Section section) {
        section.setId(id);
        retrieveSection(id);
        repository.save(section);
    }

    @Override
    public void deleteSection(String id) {
        retrieveSection(id);
        repository.deleteById(id);
    }
}
