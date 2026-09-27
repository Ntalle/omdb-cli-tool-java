import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.io.IOException;

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

            HttpClient client = HttpClient.newHttpClient();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode root = objectMapper.readTree(response.body());

            if (root.get("Response").asText().equals("False")) {
                System.out.println("Error: " + root.get("Error").asText());
                return;
            }

            Movie movie = new Movie(
                root.get("Title").asText(),
                root.get("Year").asText(),
                root.get("Director").asText(),
                root.get("imdbRating").asText()
        );

        System.out.println("Title: " + movie.getTitle());
        System.out.println("Year: " + movie.getYear());
        System.out.println("Director: " + movie.getDirector());
        System.out.println("IMDb Rating: " + movie.getImdbRating());

        } catch (IOException e) {

            System.out.println("Could not connect to OMDb.");
            System.out.println("Please check your internet connection.");

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            System.out.println("The request was interrupted.");
        }
    }
}