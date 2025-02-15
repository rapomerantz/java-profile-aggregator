# Spring Boot Profile Aggregator
This application is a Spring Boot REST microservice that handles the querying and aggregation of user profile data from external services (e.g. Github or Gitlab). It exposes a `/profile` API endpoint, which triggers calls to these external services. It aggregates and transforms the profile data and returns it as JSON. 

Its purpose is to demonstrate sensible, modern software architecture decisions, while remaining open to future extension.

Above all, it applies my one-and-only engineering dogma: When in down, **keep it simple stupid**.

## Acceptance Criteria

A customer wants to integrate a subset of GitHub’s data into their application. They want an endpoint they can provide a username, that will then return the data in JSON format as specified below:


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
        ...
    ] 
}
```
The above example response is the result of calling the API with the username `octocat` (`/profile?username=octocat`).

In the case of this Github profile information, the data is merged after calling two separate APIs:
- `https://api.github.com/users/octocat`
- `https://api.github.com/users/octocat/repos`
    - the `repos` endpoint only returns 30 requests by default.  

In order to avoid API rate limits, the result will be cached in-memory for 5 minutes.

The project will also include unit and integration tests to prove the implementation.


## Non-functional Requirements 
As with any software product, additional non-functional requirements are also necessary. These include:
- **Organization & Design**: the code should be well organized, easily extendedable, and easily maintainable.
- **Readability**: the code should be easy to read and followed by a new developer. 
- **Documentation**: the code should be well documented, and easy to run locally.
- **Stability & Security**: the code should be capable of handling bad inputs. It should be "programmed defensively".
- **Performance**: While still prioritizing the "keep it simple stupid" approach, the code should be designed with production performance in mind. 


## Architecture Decisions
Architecture decisions were made by balancing the following:
1. Simple, sensible, reasonable
    - When in doubt, choose the most obvious approach that is still "best practice". 
    - Don't "show off" or "over-engineer" with unnecessary future-proofing or complex design patterns when a reasonable approach without them is available.
    - When a more complex approach is chosen (e.g. using WebFlux for concurant, async calls to Github instead for sequenced synchronous calls), it should be easy to justify and easy to read.
1. Extensibility and flexibility
    - What are the most likely enhancements that could happen in the future? 
    - How can the simplest approach still take these into account?

### Architecture Diagram 

### Key decisions
 - Reactive programming (WebFlux)
 - In-Memory caching
 - API Client design 
 - Where to handle key mapping? 

### Logical next steps


## Running the Application