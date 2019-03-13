# OIDC 

## Pre-requisites

- [Docker Engine](https://store.docker.com/search?type=edition&offering=community)
- [Maven 3] (https://maven.apache.org/download.cgi)


## Project Structure

This project consists of two modules:

**oauth-login**

Acts as an OAuth Client.  The client is authorized by the resource owner to access their protected resources.


**resource-server**

A service which understands how to parse JWT tokens in order to extract OIDC claims so it can be mapped to `GrantedAuthorities` which are in turn used to perform authorisation checks of the end user against service endpoints.

The demo utilizes the [Authorization Code Grant Type](https://oauth.net/2/grant-types/authorization-code/).  The `oauth-login` client exchanges the passed in authorization code for an access token.   

## Identity Provider

KeyCloak Identity Provider is used to store the end user and their claims.

The idp can be located at: `localhost:8080`

## Installation

Start the KeyCloak Identity Provider.  This contains the employee information and their claims for a fictitious company called *acme*

- ```docker run -p 8080:8080 -d jonpoulter/keycloak:oidc-demo```

- Start the `oauth-login` service `springfive.oauth.OAuthLoginApplication`

- Start the `resource-server` service `springfive.security.resourceserver.ResourceServerApplication`

## Running the App

1.  Login to the application by navigating to `localhost:4000/my-message/5`
2.  Select `http://idp:8080/auth/realms/acme` link on Login Screen.
3.  Login as jonathan@acme.





 