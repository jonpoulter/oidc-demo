package springfive.security.oauth.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;
import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;

@RestController
public class UserController {


    Logger log = LoggerFactory.getLogger(this.getClass());

    private final WebClient webClient;

    public UserController(WebClient webClient) {
        this.webClient = webClient;
    }

    /** Spring injects the OAuth2AuthenticationToken */
    @GetMapping("/user")
    Map<String, Object> user(OAuth2AuthenticationToken authenticationToken) {


        return authenticationToken.getPrincipal().getAttributes();

    }

    //Mapping of currently logged in user and client registration id to an access token.
    //Asking SpringMVC to resolve the OAuth2AuthorizedClient from the keycloak IdP.
    //A client is considered "authorized" when the Resource Owner (User) has granted authorization to the client (application) to access end user resources in keycloak (as defined as scopes).
    @GetMapping("/message/annotation/keycloak/{id}")
    Mono<String> annotationKeyCloak(@PathVariable String id, @RegisteredOAuth2AuthorizedClient("keycloak") OAuth2AuthorizedClient authorizedClient) {
        log.info("Token: " + authorizedClient.getAccessToken().getTokenValue());
        log.info("Expires: " + authorizedClient.getAccessToken().getExpiresAt().toString());
        return this.webClient.get()
                .uri("http://localhost:5000/" + id)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String.class);
    }

    //Accessing the keycloak access token via the clientRegistrationId.  This is important if you use more than one IdP and you need to explicitly set the IdP access token needed downstream.
    @GetMapping("/message/keycloak/{id}")
    Mono<String> keyCloak(@PathVariable("id") String id) {
        return this.webClient.get()
                .uri("http://localhost:5000/" + id)
                .attributes(clientRegistrationId("keycloak"))
                .retrieve()
                .bodyToMono(String.class);
    }

    //Accessing the access token using the currently logged in user (IdP we logged in with)
    @GetMapping("/message/{id}")
    Mono<String> implied(@PathVariable("id") String id) {
        return this.webClient.get()
                .uri("http://localhost:5000/" + id)
                .retrieve()
                .bodyToMono(String.class);
    }


    @GetMapping("/my-message/{id}")
    Mono<String> impliedMyMessage(@PathVariable("id") String id) {
        return this.webClient.get()
                .uri("http://localhost:5000/my-message/" + id)
                .retrieve()
                .bodyToMono(String.class);
    }


    /*@GetMapping("/")
    Map<String, String>  message(@AuthenticationPrincipal Jwt jwt) {

        return Collections.singletonMap("text", String.format("This is oidc-server.  Hello %s.  Your roles are %s",
                jwt.getClaims().get("email"),
                jwt.getClaims().get("resource_access")));
    }*/

    @GetMapping("/")
    String getAuthenticationName(Authentication authentication) {

        return authentication.getName();
    }



}
