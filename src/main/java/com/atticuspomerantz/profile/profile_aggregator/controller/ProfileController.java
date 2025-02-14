package com.atticuspomerantz.profile.profile_aggregator.controller;

import org.springframework.web.bind.annotation.RestController;

import com.atticuspomerantz.profile.profile_aggregator.model.GithubUserProfile;
import com.atticuspomerantz.profile.profile_aggregator.service.ProfileService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<GithubUserProfile> getProfile(@RequestParam String username) {
        GithubUserProfile profile = profileService.getGithubProfile(username);

        return profile != null ? ResponseEntity.ok(profile) : ResponseEntity.notFound().build();
    }
}
