package main.java;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {

    public static void main(String[] args) throws Exception {

        String apiKey = System.getenv("OMDB_API_KEY");

        String url = "https://www.omdbapi.com/?apikey="
                + apiKey
                + "&t=Inception";

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

        System.out.println("Status Code: " + response.statusCode());
        System.out.println(response.body());
    }
}