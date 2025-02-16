package com.atticuspomerantz.profile.profile_aggregator.service;

import com.atticuspomerantz.profile.profile_aggregator.client.GithubApiClient;
import com.atticuspomerantz.profile.profile_aggregator.model.GithubApiUserResponse;
import com.atticuspomerantz.profile.profile_aggregator.model.GithubApiReposResponse;
import com.atticuspomerantz.profile.profile_aggregator.model.GithubUserProfile;
import com.atticuspomerantz.profile.profile_aggregator.model.GithubUserRepo;
import com.atticuspomerantz.profile.profile_aggregator.util.CacheUtility;
import com.atticuspomerantz.profile.profile_aggregator.exception.UserNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private GithubApiClient githubApiClient;

    @Mock
    private CacheUtility<GithubUserProfile> cacheUtility;

    @InjectMocks
    private ProfileService profileService;

    private GithubApiUserResponse mockUser;
    private List<GithubApiReposResponse> mockRepos;
    private GithubUserProfile expectedProfile;

    @BeforeEach
    void setUp() {
        mockUser = GithubApiUserResponse.builder()
                .userName("octocat")
                .displayName("Octo Cat")
                .avatar("avatar_url")
                .geoLocation("San Francisco")
                .email("octocat@example.com")
                .url("github.com/octocat")
                .createdAt(null)
                .build();

        mockRepos = Collections.singletonList(
                GithubApiReposResponse.builder()
                        .name("repo1")
                        .url("github.com/octocat/repo1")
                        .build()
        );

        expectedProfile = GithubUserProfile.builder()
                .userName("octocat")
                .displayName("Octo Cat")
                .avatar("avatar_url")
                .geoLocation("San Francisco")
                .email("octocat@example.com")
                .url("github.com/octocat")
                .createdAt(null)
                .repos(List.of(
                        GithubUserRepo.builder()
                                .name("repo1")
                                .url("github.com/octocat/repo1")
                                .build()
                ))
                .build();
    }

    @Test
    void testGetGithubProfile_Success() {
        when(cacheUtility.get("octocat")).thenReturn(null);
        when(githubApiClient.fetchUserDetails("octocat")).thenReturn(mockUser);
        when(githubApiClient.fetchUserRepositories("octocat")).thenReturn(mockRepos);

        GithubUserProfile profile = profileService.getGithubProfile("octocat");

        assertNotNull(profile);
        assertThat(profile)
            .usingRecursiveComparison()
            .isEqualTo(expectedProfile);
    }

    @Test
    void testGetGithubProfile_UserNotFound() {
        when(githubApiClient.fetchUserDetails("unknown")).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> profileService.getGithubProfile("unknown"));

        verify(githubApiClient, times(1)).fetchUserDetails("unknown");
        verifyNoMoreInteractions(githubApiClient);
    }

    @Test
    void testGetGithubProfile_CachedData() {
        when(cacheUtility.get("octocat")).thenReturn(expectedProfile);

        GithubUserProfile profile = profileService.getGithubProfile("octocat");

        assertNotNull(profile);
        assertThat(profile)
            .usingRecursiveComparison()
            .isEqualTo(expectedProfile);
        verifyNoInteractions(githubApiClient); // No API calls if cached
    }

    @Test
    void testGetGithubProfile_ReposFail() {
        when(githubApiClient.fetchUserDetails("octocat")).thenReturn(mockUser);
        when(githubApiClient.fetchUserRepositories("octocat")).thenThrow(new RuntimeException("GitHub API error"));
    
        GithubUserProfile profile = profileService.getGithubProfile("octocat");
    
        assertNotNull(profile);
        assertEquals("octocat", profile.getUserName());
        assertTrue(profile.getRepos().isEmpty());
    }
}