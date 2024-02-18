package com.job.project.githubrepositories.forks.service;

import com.job.project.githubrepositories.forks.model.Brunch;
import com.job.project.githubrepositories.forks.model.GithubRepos;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ForksService {

    private final RestTemplate restTemplate;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10); // Adjust the number of threads as needed

    public List<GithubRepos> getAllForksForUser(String username) throws HttpClientErrorException {
        String uri = "https://api.github.com/users/" + username + "/repos";

        try {
            String result = restTemplate.getForObject(uri, String.class);

            JSONArray repositories = new JSONArray(result);

            List<CompletableFuture<GithubRepos>> futures = new ArrayList<>();

            for (int i = 0; i < repositories.length(); i++) {
                JSONObject repo = (JSONObject) repositories.get(i);
                if (!repo.getBoolean("fork")) {
                    CompletableFuture<GithubRepos> future = CompletableFuture.supplyAsync(() -> {
                        GithubRepos userRepo = null;
                        try {
                            userRepo = fetchUserRepo(repo);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return userRepo;
                    }, executorService);
                    futures.add(future);
                }
            }

            return futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        } catch (RestClientException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "GitHub API is not available at the moment. Please try again later.");
        } catch (JSONException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while processing GitHub API response.");
        }
    }

    private GithubRepos fetchUserRepo(JSONObject repo) throws JSONException {
        String name = repo.getString("name");
        String owner = repo.getJSONObject("owner").getString("login");
        String branchUrl = repo.getString("branches_url").replaceAll("\\{.*\\}", "");
        List<Brunch> brunches = getBrunches(branchUrl);
        return GithubRepos.builder().name(name).owner(owner).brunches(brunches).build();
    }

    public List<Brunch> getBrunches(String url) {
        String result = restTemplate.getForObject(url, String.class);
        List<Brunch> userBrunches = new ArrayList<>();

        JSONArray brunches = new JSONArray(result);
        for (int i = 0; i < brunches.length(); i++) {
            JSONObject b = (JSONObject) brunches.get(i);
            userBrunches.add(Brunch.builder()
                    .name(b.getString("name"))
                    .sha(b.getJSONObject("commit").getString("sha"))
                    .build());
        }
        return userBrunches;
    }
}
