import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {

    private final String title;
    private final String year;
    private final String director;
    private final String imdbRating;

    @JsonCreator
    public Movie(
            @JsonProperty("Title") String title,
            @JsonProperty("Year") String year,
            @JsonProperty("Director") String director,
            @JsonProperty("imdbRating") String imdbRating) {

        this.title = title;
        this.year = year;
        this.director = director;
        this.imdbRating = imdbRating;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getDirector() {
        return director;
    }

    public String getImdbRating() {
        return imdbRating;
    }
}