package com.atticuspomerantz.profile.profile_aggregator.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class GithubApiResponse {
    private String login; //user_name
    private String name; //display_name
    private String avatar_url; //avatar
    private String location; //geo_location
    private String email; //email
    private String html_url; //url
    private LocalDateTime created_at; //created_at
}
