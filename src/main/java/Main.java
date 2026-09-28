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

        System.out.print("Enter movie title to search: ");
        String searchTerm = scanner.nextLine().trim();

        if (searchTerm.isBlank()) {
            System.out.println("Search term cannot be empty.");
            return;
        }

        OmdbClient omdbClient = new OmdbClient(apiKey);

        try {

            String responseBody = omdbClient.searchMovies(searchTerm);

            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode root = objectMapper.readTree(responseBody);

            if (root.get("Response").asText().equals("False")) {
                System.out.println(
                        "Error: " + root.get("Error").asText()
                );
                return;
            }

            JsonNode searchResults = root.get("Search");

            System.out.println("\nSearch Results:");

            for (int i = 0; i < searchResults.size(); i++) {

                JsonNode result = searchResults.get(i);

                System.out.println(
                        (i + 1)
                                + ". "
                                + result.get("Title").asText()
                                + " ("
                                + result.get("Year").asText()
                                + ")"
                );

                System.out.println(
                        "   IMDb ID: "
                                + result.get("imdbID").asText()
                );
            }

            System.out.print("\nChoose a movie number: ");
            String choiceInput = scanner.nextLine().trim();

            int choice = Integer.parseInt(choiceInput);

            if (choice < 1 || choice > searchResults.size()) {
                System.out.println("Invalid movie selection.");
                return;
            }

            JsonNode selectedMovie =
                    searchResults.get(choice - 1);

            String imdbId =
                    selectedMovie.get("imdbID").asText();

            String movieResponse =
                    omdbClient.getMovieById(imdbId);

            JsonNode movieRoot =
                    objectMapper.readTree(movieResponse);

            if (movieRoot.get("Response").asText().equals("False")) {
                System.out.println(
                        "Error: " + movieRoot.get("Error").asText()
                );
                return;
            }

            Movie movie =
                    objectMapper.readValue(
                            movieResponse,
                            Movie.class
                    );

            System.out.println("\nMovie Details:");
            System.out.println("Title: " + movie.getTitle());
            System.out.println("Year: " + movie.getYear());
            System.out.println("Director: " + movie.getDirector());
            System.out.println("IMDb Rating: " + movie.getImdbRating());

        } catch (NumberFormatException e) {

            System.out.println("Please enter a valid number.");

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