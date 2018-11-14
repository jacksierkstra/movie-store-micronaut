package movie.store.micronaut.repository;

import io.micronaut.configuration.hibernate.jpa.scope.CurrentSession;
import io.micronaut.spring.tx.annotation.Transactional;
import movie.store.micronaut.ApplicationConfiguration;
import movie.store.micronaut.model.Movie;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Singleton
public class MovieRepositoryImpl implements MovieRepository {

    @PersistenceContext
    private EntityManager entityManager;
    private final ApplicationConfiguration applicationConfiguration;

    public MovieRepositoryImpl(@CurrentSession EntityManager entityManager,
                               ApplicationConfiguration applicationConfiguration) {
        this.entityManager = entityManager;
        this.applicationConfiguration = applicationConfiguration;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Movie> findById(@NotNull Long id) {
        return Optional.ofNullable(entityManager.find(Movie.class, id));
    }

    @Override
    @Transactional
    public Movie save(Movie movie) {
        entityManager.persist(movie);
        return movie;
    }

    @Override
    @Transactional
    public void deleteById(@NotNull Long id) {
        findById(id).ifPresent(movie -> entityManager.remove(movie));
    }

    private final static List<String> VALID_PROPERTY_NAMES = Arrays.asList("id", "title");

    @Override
    @Transactional(readOnly = true)
    public List<Movie> findAll(@NotNull SortingAndOrderArguments args) {
        String qlString = "SELECT m FROM movie as m";
        if (args.getOrder().isPresent() && args.getSort().isPresent() && VALID_PROPERTY_NAMES.contains(args.getSort().get())) {
            qlString += " ORDER BY m." + args.getSort().get() + " " + args.getOrder().get().toLowerCase();
        }
        TypedQuery<Movie> query = entityManager.createQuery(qlString, Movie.class);
        query.setMaxResults(args.getMax().orElseGet(applicationConfiguration::getMax));
        args.getOffset().ifPresent(query::setFirstResult);
        return query.getResultList();
    }
}
