package movie.store.micronaut.repository;



import movie.store.micronaut.model.Movie;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface MovieRepository {

    Optional<Movie> findById(@NotNull Long id);

    Movie save(Movie movie);

    void deleteById(@NotNull Long id);

    List<Movie> findAll(@NotNull SortingAndOrderArguments args);
}
