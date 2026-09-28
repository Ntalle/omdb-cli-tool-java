import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OmdbClient {

    private final String apiKey;
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public OmdbClient(String apiKey) {
        this.apiKey = apiKey;

        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        this.objectMapper = new ObjectMapper();
    }

    public List<SearchResult> searchMovies(String searchTerm)
            throws JsonProcessingException, IOException, InterruptedException {

        String encodedSearchTerm =
                URLEncoder.encode(searchTerm, StandardCharsets.UTF_8);

        String url = "https://www.omdbapi.com/?apikey="
                + apiKey
                + "&s="
                + encodedSearchTerm
                + "&type=movie";

        URI uri = URI.create(url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        int statusCode = response.statusCode();

        if (statusCode < 200 || statusCode >= 300) {
            throw new IOException(
                    "HTTP request failed. Status Code: " + statusCode
            );
        }

        return parseSearchResults(response.body());
    }

    public Movie getMovieById(String imdbId)
            throws JsonProcessingException, IOException, InterruptedException {

        String url = "https://www.omdbapi.com/?apikey="
                + apiKey
                + "&i="
                + imdbId;

        URI uri = URI.create(url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        int statusCode = response.statusCode();

        if (statusCode < 200 || statusCode >= 300) {
            throw new IOException(
                    "HTTP request failed. Status Code: " + statusCode
            );
        }

        return parseMovie(response.body());
    }

    private List<SearchResult> parseSearchResults(String responseBody)
            throws JsonProcessingException {

        JsonNode root = objectMapper.readTree(responseBody);

        if (root.get("Response").asText().equals("False")) {
            throw new IllegalStateException(
                    root.get("Error").asText()
            );
        }

        JsonNode searchResults = root.get("Search");

        List<SearchResult> results = new ArrayList<>();

        for (int i = 0; i < searchResults.size(); i++) {

            JsonNode result = searchResults.get(i);

            SearchResult searchResult = new SearchResult(
                    result.get("Title").asText(),
                    result.get("Year").asText(),
                    result.get("imdbID").asText()
            );

            results.add(searchResult);
        }

        return results;
    }

    private Movie parseMovie(String responseBody)
            throws JsonProcessingException {

        JsonNode root = objectMapper.readTree(responseBody);

        if (root.get("Response").asText().equals("False")) {
            throw new IllegalStateException(
                    root.get("Error").asText()
            );
        }

        return objectMapper.readValue(responseBody, Movie.class);
    }
}