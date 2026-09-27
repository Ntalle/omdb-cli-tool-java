import java.io.IOException;
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

        OmdbClient omdbClient = new OmdbClient(apiKey);

        try {

            String responseBody = omdbClient.searchMovie(movieTitle);

            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode root = objectMapper.readTree(responseBody);

            if (root.get("Response").asText().equals("False")) {
                
                System.out.println("Error: " + root.get("Error").asText());
                return;
            }

            Movie movie = objectMapper.readValue(responseBody, Movie.class);

            System.out.println("Title: " + movie.getTitle());
            System.out.println("Year: " + movie.getYear());
            System.out.println("Director: " + movie.getDirector());
            System.out.println("IMDb Rating: " + movie.getImdbRating());

        } catch (JsonProcessingException e) {

            System.out.println("Could not process the movie data.");
            System.out.println("Reason: " + e.getMessage());

        } catch (IOException e) {

            System.out.println(
                    "Could not complete the network request."
            );
            System.out.println("Reason: " + e.getMessage());

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            System.out.println("The request was interrupted.");
        }
    }
}