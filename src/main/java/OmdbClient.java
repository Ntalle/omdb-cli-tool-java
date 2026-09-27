import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class OmdbClient {

    private final String apiKey;
    private final HttpClient client;

    public OmdbClient(String apiKey) {
        this.apiKey = apiKey;

        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public String searchMovie(String movieTitle)
            throws IOException, InterruptedException {

        String encodedTitle =
                URLEncoder.encode(movieTitle, StandardCharsets.UTF_8);

        String url = "https://www.omdbapi.com/?apikey="
                + apiKey
                + "&t="
                + encodedTitle;

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

        return response.body();
    }
}