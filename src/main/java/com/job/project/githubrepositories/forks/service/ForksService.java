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

@Service
@RequiredArgsConstructor
public class ForksService {

    private final RestTemplate restTemplate;

    public List<GithubRepos> getAllForksForUser(String username) throws HttpClientErrorException {
        String uri = "https://api.github.com/users/" + username + "/repos";

        try {
            String result = restTemplate.getForObject(uri, String.class);

            List<GithubRepos> userRepos = new ArrayList<>();

            JSONArray repositories = new JSONArray(result);
            for (int i = 0; i < repositories.length(); i++) {
                JSONObject repo = (JSONObject) repositories.get(i);
                if (!repo.getBoolean("fork")) {
                    GithubRepos userRepo = GithubRepos.builder()
                            .name(repo.getString("name"))
                            .owner(repo.getJSONObject("owner").getString("login"))
                            .build();
                    String branchUrl = repo.getString("branches_url").replaceAll("\\{.*\\}", "");
                    userRepo.setBrunches(getBrunches(branchUrl));
                    userRepos.add(userRepo);
                }
            }

            return userRepos;
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
