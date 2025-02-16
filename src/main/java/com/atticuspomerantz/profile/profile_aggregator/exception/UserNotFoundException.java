package com.atticuspomerantz.profile.profile_aggregator.exception;

/**
 * Exception thrown when a GitHub user is not found.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("GitHub profile not found for user: " + username);
    }
}