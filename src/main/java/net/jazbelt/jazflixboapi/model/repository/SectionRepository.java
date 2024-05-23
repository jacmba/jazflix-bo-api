package net.jazbelt.jazflixboapi.model.repository;

import net.jazbelt.jazflixboapi.model.entity.Section;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends MongoRepository<Section, String> {
}
