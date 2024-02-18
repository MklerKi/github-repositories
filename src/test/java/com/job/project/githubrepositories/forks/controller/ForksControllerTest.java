package com.job.project.githubrepositories.forks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.job.project.githubrepositories.forks.model.GithubRepos;
import com.job.project.githubrepositories.forks.service.ForksService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ForksController.class)
public class ForksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ForksService forksService;

    @Test
    public void testGetAllForksForUser() throws Exception {
        // Given
        List<GithubRepos> expectedRepos = List.of(
                GithubRepos.builder()
                        .name("userRepo1")
                        .owner("userLogin")
                        .brunches(new ArrayList<>())
                        .build());
        when(forksService.getAllForksForUser("testUser")).thenReturn(expectedRepos);

        // When
        mockMvc.perform(get("/api/v1/getAllForksForUser/{username}", "testUser")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedRepos)));
    }
}
