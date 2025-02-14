package com.atticuspomerantz.profile.profile_aggregator.model;

import lombok.Data;

@Data
public class GithubUserRepo {
    private String name;
    private String url;


    public GithubUserRepo(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
