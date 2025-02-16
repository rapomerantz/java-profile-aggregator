# Spring Boot Profile Aggregator
This application is a Spring Boot REST microservice that queries and aggregates user profile data from external services (currently Github).
It exposes a /profile/{username} API endpoint that fetches, transforms, and caches profile data.

Its purpose is to demonstrate sensible, modern software architecture decisions, while remaining open to future extension.

Above all, it applies my one-and-only engineering dogma: When in down, **keep it simple stupid**.

## Key Features
- Fetches Github user profile data and repositories
- Aggregates and formats the response as JSON
- Uses in-memory caching (5 min TTL) to reduce API rate limit issues
- Designed for simplicity, maintanability, and future extension.

## Example Response

Calling `/profile/octocat` returns:


```json
{
    user_name: "octocat",
    display_name: "The Octocat",
    avatar: "https://avatars3.githubusercontent.com/u/583231?v=4", geo_location: "San Francisco",
    email: null,
    url: "https://github.com/octocat ",
    created_at: "2011-01-25 18:44:36",
    repos: [{
        name: "boysenberry-repo-1",
        url: "https://github.com/octocat/boysenberry-repo-1" }, 
       // ...
    ] 
}
```

The data is aggregated after calling two separate APIs:
- `https://api.github.com/users/octocat`
- `https://api.github.com/users/octocat/repos`
    - the `repos` endpoint only returns 30 requests by default. 


## Installation and Setup

1. Clone the repository
```
git clone https://github.com/rapomerantz/java-profile-aggregator.git
cd java-profile-aggregator
```
1. Build the project
```
./mvnw clean install 
```
1. Run the application
```
./mvnw spring-boot:run
```
By default, the service runs on `http://localhost:8080`

## API Usage
### Fetch a Github Profile
```
curl -X GET "http://localhost:8080/profile/octocat"
```
- Expected response: Returns GitHub user details and repositories (example above)
- If the user is not found: Returns `404` 
- If the request fails: returns `500` 

## Running Tests
1. Run all tests
```
./mvnw test
```
1. Run unit tests only
```
./mvnw -Dtest=ProfileServiceTest test
```
1. Run all tests
```
./mvnw -Dtest=ProfileControllerTest test 
```


### Architecture Diagram 

![Architecture](assets/profile_utility_architecture.jpg)