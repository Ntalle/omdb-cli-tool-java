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

        OmdbClient omdbClient = new OmdbClient(apiKey);

        while (true) {

            System.out.print("\nEnter movie title to search: ");
            String searchTerm = scanner.nextLine().trim();

            if (searchTerm.equalsIgnoreCase("Q")) {
                System.out.println("Goodbye!");
                break;
            }

            if (searchTerm.isBlank()) {
                System.out.println("Search term cannot be empty.");
                continue;
            }

            int page = 1;

            try {

                while (true) {

                    SearchResultPage searchResultPage =
                            omdbClient.searchMovies(
                                    searchTerm,
                                    page
                            );

                    List<SearchResult> results =
                            searchResultPage.getResults();

                    int totalResults =
                            searchResultPage.getTotalResults();

                    int totalPages =
                            (totalResults + 9) / 10;

                    System.out.println(
                            "\n===== Page "
                                    + page
                                    + " of "
                                    + totalPages
                                    + " ====="
                    );

                    System.out.println(
                            "Total results: "
                                    + totalResults
                    );

                    for (int i = 0; i < results.size(); i++) {

                        SearchResult result =
                                results.get(i);

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

                    System.out.println("\nN. Next page");
                    System.out.println("P. Previous page");
                    System.out.println("S. New search");
                    System.out.println("Q. Quit");

                    System.out.print("\nChoose an option: ");
                    String choiceInput =
                            scanner.nextLine().trim();

                    if (choiceInput.equalsIgnoreCase("N")) {

                        if (page < totalPages) {
                            page++;
                        } else {
                            System.out.println(
                                    "You are already on the last page."
                            );

                            System.out.print(
                                    "Press Enter to continue..."
                            );
                            scanner.nextLine();
                        }

                    } else if (choiceInput.equalsIgnoreCase("P")) {

                        if (page > 1) {
                            page--;
                        } else {
                            System.out.println(
                                    "You are already on the first page."
                            );

                            System.out.print(
                                    "Press Enter to continue..."
                            );
                            scanner.nextLine();
                        }

                    } else if (choiceInput.equalsIgnoreCase("S")) {

                        break;

                    } else if (choiceInput.equalsIgnoreCase("Q")) {

                        System.out.println("Goodbye!");
                        return;

                    } else {

                        try {

                            int choice =
                                    Integer.parseInt(choiceInput);

                            if (choice < 1
                                    || choice > results.size()) {

                                System.out.println(
                                        "Invalid movie selection."
                                );

                                System.out.print(
                                        "Press Enter to continue..."
                                );
                                scanner.nextLine();

                            } else {

                                SearchResult selectedResult =
                                        results.get(choice - 1);

                                String imdbId =
                                        selectedResult.getImdbId();

                                Movie movie =
                                        omdbClient.getMovieById(imdbId);

                                System.out.println(
                                        "\nMovie Details:"
                                );

                                System.out.println(
                                        "Title: "
                                                + movie.getTitle()
                                );

                                System.out.println(
                                        "Year: "
                                                + movie.getYear()
                                );

                                System.out.println(
                                        "Director: "
                                                + movie.getDirector()
                                );

                                System.out.println(
                                        "IMDb Rating: "
                                                + movie.getImdbRating()
                                );

                                System.out.println(
                                        "\nPress Enter to return to search results."
                                );

                                scanner.nextLine();
                            }

                        } catch (NumberFormatException e) {

                            System.out.println(
                                    "Please enter a valid option."
                            );

                            System.out.print(
                                    "Press Enter to continue..."
                            );
                            scanner.nextLine();
                        }
                    }
                }

            } catch (IllegalStateException e) {

                System.out.println(
                        "OMDb Error: "
                                + e.getMessage()
                );

            } catch (JsonProcessingException e) {

                System.out.println(
                        "Could not process the movie data."
                );

                System.out.println(
                        "Reason: "
                                + e.getMessage()
                );

            } catch (IOException e) {

                System.out.println(
                        "Could not complete the network request."
                );

                System.out.println(
                        "Reason: "
                                + e.getMessage()
                );

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                System.out.println(
                        "The request was interrupted."
                );
            }
        }
    }
}