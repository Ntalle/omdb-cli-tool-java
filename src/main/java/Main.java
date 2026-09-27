import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {

    public static void main(String[] args) {

        String apiKey = System.getenv("OMDB_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            System.out.println("API key not found.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter movie title: ");
        String movieTitle = scanner.nextLine().trim();

        String encodedTitle =
                URLEncoder.encode(movieTitle, StandardCharsets.UTF_8);

        String url = "https://www.omdbapi.com/?apikey="
                + apiKey
                + "&t="
                + encodedTitle;

        try {

            URI uri = URI.create(url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            int statusCode = response.statusCode();

            if (statusCode < 200 || statusCode >= 300) {
                System.out.println("HTTP request failed.");
                System.out.println("Status Code: " + statusCode);
                return;
            }

            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode root = objectMapper.readTree(response.body());

            if (root.get("Response").asText().equals("False")) {
                System.out.println("Error: " + root.get("Error").asText());
                return;
            }

            Movie movie = objectMapper.readValue(response.body(), Movie.class);

            System.out.println("Title: " + movie.getTitle());
            System.out.println("Year: " + movie.getYear());
            System.out.println("Director: " + movie.getDirector());
            System.out.println("IMDb Rating: " + movie.getImdbRating());

        } catch (JsonProcessingException e) {

            System.out.println("Could not process the movie data.");
            System.out.println("Reason: " + e.getMessage());

        } catch (IOException e) {

            System.out.println("Could not complete the network request.");
            System.out.println("Reason: " + e.getMessage());

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            System.out.println("The request was interrupted.");
        }
    }
}