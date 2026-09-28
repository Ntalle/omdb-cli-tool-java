import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.JsonProcessingException;

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

            List<SearchResult> results =
                    omdbClient.searchMovies(searchTerm);

            System.out.println("\nSearch Results:");

            for (int i = 0; i < results.size(); i++) {

                SearchResult result = results.get(i);

                System.out.println(
                        (i + 1)
                                + ". "
                                + result.getTitle()
                                + " ("
                                + result.getYear()
                                + ")"
                );

                System.out.println(
                        "   IMDb ID: "
                                + result.getImdbId()
                );
            }

            System.out.print("\nChoose a movie number: ");
            String choiceInput = scanner.nextLine().trim();

            int choice = Integer.parseInt(choiceInput);

            if (choice < 1 || choice > results.size()) {
                System.out.println("Invalid movie selection.");
                return;
            }

            SearchResult selectedResult =
                    results.get(choice - 1);

            String imdbId = selectedResult.getImdbId();

            Movie movie =
                    omdbClient.getMovieById(imdbId);

            System.out.println("\nMovie Details:");
            System.out.println("Title: " + movie.getTitle());
            System.out.println("Year: " + movie.getYear());
            System.out.println("Director: " + movie.getDirector());
            System.out.println(
                    "IMDb Rating: " + movie.getImdbRating()
            );

        } catch (NumberFormatException e) {

            System.out.println("Please enter a valid number.");

        } catch (IllegalStateException e) {

            System.out.println("OMDb Error: " + e.getMessage());

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