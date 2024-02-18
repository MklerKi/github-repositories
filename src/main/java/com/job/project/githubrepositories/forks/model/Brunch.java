package com.job.project.githubrepositories.forks.model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@Data
@ToString
public class Brunch {
    String name;
    String sha;
}
