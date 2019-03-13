package springfive.security.oauth.infra;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Class that overrides Spring Boot Auto-Configuration class for OAuth Client (OAuth2ClientAutoConfiguration)
 * and OAuth Resource Server
 * This is because this OAuth2.0 Client performs both OAuth2.0 Login and Resource Server Semantics.
 *
 * By default OAuth Client:
 *
 * 1. registers a ClientRegistrationRepository @Bean composed of ClientRegistration(s) fro the configured OAuth Client properties
 * 2. provides a WebSecurityConfigurerAdapter @Configuration and enables OAuth 2.0 Login through httpSecurity.oauthLogin().
 *
 *  Step 1 is usually done by using existing Bean and simply configuring its properties via application.yaml
 *
 *  Step 2 -> http
 *                 .authorizeRequests()
 *                     .anyRequest().authenticated()
 *                     .and()
 *                 .oauth2Login();
 *
 *  By default Resource Server:
 *
 *  1.  provides a WebSecurityConfigurerAdapter that configures the app as a resource server.
 *  2.  provides a JwtDecoder which decodes String tokens into validated instances of Jwt.
 *
 *  Step 1 ->  http
 *         .authorizeRequests()
 *             .anyRequest().authenticated()
 *             .and()
 *         .oauth2ResourceServer()
 *             .jwt();
 *
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     * OAuth 2.0 Login feature provides an application with capability to have users log in to the application by using
     * their existing account at an OAuth 2.0 Provider (GitHub) or OpenID Connect 1.0 Provider (such as Google, AD, KeyCloak)
     *
     * The OAuth 2.0 Login is implemented by using the <b>Authorisation Code Grant</b>.
     *
     * 1.   Create OAuth 2.0 Credentials within your IdP (client-id and client-secret)
     * 2.   In IdP, Set redirect URI to be of form {baseUrl}/login/oauth2/code/{registrationId} where registrationId is a unique identifier for the Client Registration (google, keycloak etc)
     * 3.   Configure application.yaml to use the OAuth Client for the authentication flow.
     *
     * for more info please refer to https://docs.spring.io/spring-security/site/docs/current/reference/html/jc.html
     *
     */

    /**
     * This enables auth2Logins but also authorisation code grants even if we are not trying to login.
     * KeyCloak access token isn't necessary - as the identity is provided by some other means (another identity provider)
     * We've enabled getting an access token outside of login, Spring Security can get the access token for us.
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.oauth2Login().and()
                .authorizeRequests()
                .anyRequest().authenticated().and().oauth2Client().authorizationCodeGrant();
                //.and()
               // .oauth2ResourceServer()
                //.jwt();
                //.oauth2Client()
                //.authorizationCodeGrant();
    }
}
