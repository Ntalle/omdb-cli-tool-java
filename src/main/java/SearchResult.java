public class SearchResult {

    private final String title;
    private final String year;
    private final String imdbId;

    public SearchResult(String title, String year, String imdbId) {
        this.title = title;
        this.year = year;
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getImdbId() {
        return imdbId;
    }
}