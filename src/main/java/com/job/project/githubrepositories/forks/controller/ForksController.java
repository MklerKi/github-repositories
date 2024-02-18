package com.job.project.githubrepositories.forks.controller;

import com.job.project.githubrepositories.forks.model.GithubRepos;
import com.job.project.githubrepositories.forks.service.ForksService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ForksController {

    private final ForksService forksService;

    private static Logger logger = LoggerFactory.getLogger(ForksController.class);

    @GetMapping(value = "/getAllForksForUser/{username}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<GithubRepos> getAllForksForUser(@PathVariable("username") String username) {
        logger.info("getAllForksForUser get request for " + username + " user");
        return forksService.getAllForksForUser(username);
    }
}
