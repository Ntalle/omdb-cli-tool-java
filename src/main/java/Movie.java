public class Movie {

    private String title;
    private String year;
    private String director;
    private String imdbRating;

    public Movie(String title, String year, String director, String imdbRating) {
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