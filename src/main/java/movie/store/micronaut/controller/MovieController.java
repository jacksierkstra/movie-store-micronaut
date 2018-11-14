package movie.store.micronaut.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.validation.Validated;
import movie.store.micronaut.model.Movie;
import movie.store.micronaut.repository.MovieRepository;
import movie.store.micronaut.repository.SortingAndOrderArguments;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Validated
@Controller("/api/movies")
public class MovieController {

    private final MovieRepository movieRepository;

    public MovieController(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Get("{?args}")
    public HttpResponse<List<Movie>> getBooks(@Nullable @Valid SortingAndOrderArguments args) {
        SortingAndOrderArguments arguments = args;
        if (arguments == null) {
            arguments = new SortingAndOrderArguments();
        }
        return HttpResponse.ok(this.movieRepository.findAll(arguments));
    }

    @Get("/{bookId}")
    public HttpResponse<Optional<Movie>> getBook(Long bookId) {
        return HttpResponse.ok(this.movieRepository.findById(bookId));
    }

    @Post
    public HttpResponse<Movie> addBook(@Body @Valid Movie movie) {
        Movie createdMovie = this.movieRepository.save(movie);

        return HttpResponse.created(URI.create(String.format("/api/movies/%s", createdMovie.id)));
    }

    @Delete("/{bookId}")
    public void deleteBook(Long bookId) {
        this.movieRepository.deleteById(bookId);
    }
}
