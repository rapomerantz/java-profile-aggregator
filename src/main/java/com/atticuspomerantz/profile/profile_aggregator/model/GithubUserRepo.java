package com.atticuspomerantz.profile.profile_aggregator.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GithubUserRepo {
    private String name;
    private String url;
}
