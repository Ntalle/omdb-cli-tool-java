import java.util.List;

public class SearchResultPage {

    private final List<SearchResult> results;
    private final int totalResults;

    public SearchResultPage(List<SearchResult> results,int totalResults) {

        this.results = results;
        this.totalResults = totalResults;
    }

    public List<SearchResult> getResults() {
        return results;
    }

    public int getTotalResults() {
        return totalResults;
    }
}