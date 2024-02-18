package com.job.project.githubrepositories.forks.service;

import com.job.project.githubrepositories.forks.model.Brunch;
import com.job.project.githubrepositories.forks.model.GithubRepos;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ForksServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Spy
    @InjectMocks
    private ForksService forksService;

    @Test
    public void testGetAllForksForUser_Success() {
        // Given
        String username = "testUser";
        List<Brunch> brunches = List.of(Brunch.builder().name("brunchName").sha("sha").build());
        List<GithubRepos> githubRepos = List.of(
                GithubRepos.builder().name("userRepo1").owner("userLogin").brunches(brunches).build());
        String repoJson = "[{\"name\": \"userRepo1\", \"owner\": {\"login\": \"userLogin\"}, \"fork\": false, \"branches_url\": \"https://api.github.com/repos/userLogin/userRepo1/branches{/branch}\"}]";
        when(restTemplate.getForObject("https://api.github.com/users/" + username + "/repos", String.class))
                .thenReturn(repoJson);
        doReturn(brunches)
                .when(forksService)
                .getBrunches("https://api.github.com/repos/userLogin/userRepo1/branches");


        // When
        List<GithubRepos> result = forksService.getAllForksForUser(username);

        // Then
        assertEquals(1, result.size());
        assertEquals(githubRepos, result);
    }

    @Test
    void testGetAllForksForUser_UserNotFound() {
        // Given
        String username = "nonexistentUser";
        String uri = "https://api.github.com/users/" + username + "/repos";
        when(restTemplate.getForObject(uri, String.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // When
        Exception exception = assertThrows(ResponseStatusException.class, () ->
                forksService.getAllForksForUser(username));

        // Then
        assertEquals(HttpStatus.NOT_FOUND, ((ResponseStatusException) exception).getStatusCode());
        assertEquals("User not found", ((ResponseStatusException) exception).getReason());
    }

    @Test
    void testGetAllForksForUser_APINotAvailable() {
        // Given
        String username = "username";
        String uri = "https://api.github.com/users/" + username + "/repos";
        when(restTemplate.getForObject(uri, String.class))
                .thenThrow(new RestClientException("GitHub API is not available"));

        // When
        Exception exception = assertThrows(ResponseStatusException.class, () ->
                forksService.getAllForksForUser(username));

        // Then
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, ((ResponseStatusException) exception).getStatusCode());
        assertEquals("GitHub API is not available at the moment. Please try again later.", ((ResponseStatusException) exception).getReason());
    }

    @Test
    void testGetAllForksForUser_JSONException() {
        // Given
        String username = "username";
        String uri = "https://api.github.com/users/" + username + "/repos";
        when(restTemplate.getForObject(uri, String.class))
                .thenReturn("Invalid JSON response");

        // When
        Exception exception = assertThrows(ResponseStatusException.class, () ->
                forksService.getAllForksForUser(username));

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ((ResponseStatusException) exception).getStatusCode());
        assertEquals("Error occurred while processing GitHub API response.", ((ResponseStatusException) exception).getReason());
    }
}
