package movie.store.micronaut.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity(name = "movie")
public class Movie {

    @Id
    @GeneratedValue
    public Long id;

    public String title;
}
