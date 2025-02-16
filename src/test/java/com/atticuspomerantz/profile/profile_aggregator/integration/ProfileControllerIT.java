package com.atticuspomerantz.profile.profile_aggregator.integration;

import com.atticuspomerantz.profile.profile_aggregator.model.GithubUserProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProfileControllerIT {

    @Autowired
    private TestRestTemplate restTemplate; 


    @Test
    void testGetProfile_Success() {
        ResponseEntity<GithubUserProfile> response = restTemplate.getForEntity("/profile/octocat", GithubUserProfile.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUserName()).isEqualTo("octocat");
        assertThat(response.getBody().getDisplayName()).isEqualTo("The Octocat");
        assertThat(response.getBody().getAvatar()).isEqualTo("https://avatars.githubusercontent.com/u/583231?v=4");
        assertThat(response.getBody().getGeoLocation()).isEqualTo("San Francisco");
        assertThat(response.getBody().getEmail()).isNull();
        assertThat(response.getBody().getUrl()).isEqualTo("https://github.com/octocat");
        assertThat(response.getBody().getCreatedAt()).isEqualTo("2011-01-25T18:44:36");
        assertThat(response.getBody().getRepos()).isNotEmpty();
        assertThat(response.getBody().getRepos().get(0).getName()).isEqualTo("boysenberry-repo-1");
        assertThat(response.getBody().getRepos().get(0).getUrl()).isEqualTo("https://github.com/octocat/boysenberry-repo-1");
    }

    /**
     * Tests the case where the requested GitHub user does not exist.
     */
    @Test
    void testGetProfile_UserNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity("/profile/1234123412341234xxx444321", String.class);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(response.getBody()).contains("GitHub profile not found for user: 1234123412341234xxx444321");
    }

    /**
     * Tests a malformed request where no username is provided.
     */
    @Test
    void testGetProfile_MissingUsername() {
        ResponseEntity<String> response = restTemplate.getForEntity("/profile", String.class);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    }
}