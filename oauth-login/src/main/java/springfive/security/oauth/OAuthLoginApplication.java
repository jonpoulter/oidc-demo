package springfive.security.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class OAuthLoginApplication {

	@Bean
	WebClient webClient(ClientRegistrationRepository regs, OAuth2AuthorizedClientRepository authz) {
		//Provides an easy mechanism for using an OAuth2AuthorizedClient to make OAuth2 requests by including the token as a Bearer Token.
		//A client is considered "authorized" when the resource owner has granted authorization to the client to access it's protected resources.
		ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
				new ServletOAuth2AuthorizedClientExchangeFilterFunction(regs, authz);
		//If set, a default OAuth2AuthorizedClient can be discovered from the current Authentication.
		oauth2.setDefaultOAuth2AuthorizedClient(true);
		return WebClient.builder()
						.apply(oauth2.oauth2Configuration())
						.build();
	}

	public static void
	main(String[] args) {
		SpringApplication.run(OAuthLoginApplication.class, args);
	}

}
