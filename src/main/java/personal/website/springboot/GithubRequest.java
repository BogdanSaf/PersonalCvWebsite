package personal.website.springboot;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class GithubRequest {

    private String username;
    HttpResponse<String> response;

    private String error;

    JsonArray jsonAPIArray;

    private final HashMap<String, List<String>> repoDescription = new HashMap<String, List<String>>();
    private final HashMap<String, List<String>> repoCotributers = new HashMap<String, List<String>>();


    public GithubRequest(String username) {
        this.username = username;


    }

    public void githubCall() throws IOException, InterruptedException {
        try {
            String url = "https://api.github.com/users/" + username + "/repos";
            System.out.println(url);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/vnd.github.v3+json")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            jsonAPIArray = new Gson().fromJson(response.body(), JsonArray.class);

            getContributers();
            getRepoDescriptions();

        } catch (Exception e) {
            error = e.toString();
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getStatusCode() {
        return response.statusCode();
    }

    public HttpResponse<String> getResponse() {
        return response;
    }

    public String getError() {
        return error;
    }

    private void getRepoDescriptions() {
//        Loop through all the repos
        for (JsonElement currentObject : jsonAPIArray) {

//              Check if the description and name are null and if it is, set it to "No description"
                String description = currentObject.getAsJsonObject().has("description") && !currentObject.getAsJsonObject().get("description").isJsonNull() ? currentObject.getAsJsonObject().get("description").getAsString() : "No description";
                String name = currentObject.getAsJsonObject().has("name") && !currentObject.getAsJsonObject().get("name").isJsonNull() ? currentObject.getAsJsonObject().get("name").getAsString() : "No name";
//              Add the description to the hashmap with the name as the key
                repoDescription.put(name, List.of(description));
        }
    }

    private void getContributers() {
//        Loop through all the repos
        for (JsonElement currentObject : jsonAPIArray) {
//            Get the name and contributors_url\
            String name = currentObject.getAsJsonObject().get("name").getAsString();
            String url = currentObject.getAsJsonObject().get("contributors_url").getAsString();

//            Make a request to the contributors_url
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/vnd.github.v3+json")
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

//            Get the response and parse it
            try {
                HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

                JsonArray contributorsArray = new Gson().fromJson(response.body(), JsonArray.class);

//                Get the login of each contributor and add it to the list
                List<JsonElement> contributorsList = StreamSupport.stream(contributorsArray.spliterator(), false).toList();

//                Add the list to the hashmap by creating a list of name(login) of the contributors as the value and current repo name as the key
                repoCotributers.put(name, contributorsList.stream()
                        .map(element -> element.getAsJsonObject().get("login").getAsString())
                        .collect(Collectors.toList()));

            } catch (Exception e) {
                error += e.toString();
                System.out.println(error);
            }

        }

    }


    public HashMap<String, List<String>> getRepoDescription() {
        return repoDescription;
    }

    public HashMap<String, List<String>> getRepoContributers() {
        return repoCotributers;
    }

    public JsonArray getJsonAPIArray() {
        return jsonAPIArray;
    }


}
