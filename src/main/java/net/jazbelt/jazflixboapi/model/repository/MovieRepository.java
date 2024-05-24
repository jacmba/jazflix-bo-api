package net.jazbelt.jazflixboapi.model.repository;

import net.jazbelt.jazflixboapi.model.entity.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends MongoRepository<Movie, String> {
}
