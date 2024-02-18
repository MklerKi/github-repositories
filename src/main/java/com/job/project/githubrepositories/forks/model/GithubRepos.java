package com.job.project.githubrepositories.forks.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class GithubRepos {
    String name;
    String owner;
    List<Brunch> brunches;
}
