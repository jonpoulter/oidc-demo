package springfive.security.resourceserver;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.stereotype.Component;
import springfive.security.resourceserver.domain.repositories.AuthorityRepository;
import springfive.security.resourceserver.domain.repositories.MessageRepository;
import springfive.security.resourceserver.domain.repositories.UserRepository;
import springfive.security.resourceserver.domain.services.UserRepositoryUserDetailsService;
import springfive.security.resourceserver.domain.vo.Authority;
import springfive.security.resourceserver.domain.vo.Message;
import springfive.security.resourceserver.domain.vo.User;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.function.Function;

@EnableGlobalMethodSecurity (
		prePostEnabled = true,
		jsr250Enabled = true,
		securedEnabled = true
)
@SpringBootApplication
public class ResourceServerApplication {


	public static void main(String[] args) {
		SpringApplication.run(ResourceServerApplication.class, args);
	}


	/**
	 * Used to generate messages used by demo to illustrate how OIDC claims can be mapped to Authenticated Principal
	 */
	@Transactional
	@Component
	@Log4j2
	static class Runner implements ApplicationRunner {

		private final UserRepository userRepository;
		private final MessageRepository messageRepository;
		private final AuthorityRepository authorityRepository;
		private final UserDetailsService userDetailsService;

		public Runner(UserRepository userRepository,
					  MessageRepository messageRepository,
					  AuthorityRepository authorityRepository,
					  UserDetailsService userDetailsService)  {
			this.userRepository = userRepository;
			this.messageRepository = messageRepository;
			this.authorityRepository = authorityRepository;
			this.userDetailsService = userDetailsService;
		}

		/*
		Authenticates a user and sets the Authentication Object into the Spring SecurityContext ThreadLocal Storage.
 		 */
		private void authenticate(String username) {
		    //Uses our custom UserDetails class -> UserUserDetails.
			UserDetails user = this.userDetailsService.loadUserByUsername(username);
			Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);

		}

		private void attemptAccess(User adminUser, User regularUser, Message message, Function<Long, Optional<Message>> fn ) {

			authenticate(adminUser.getEmail());
			log.info("message is " + fn.apply(message.getId()).get().getText());
			//log.info("message is " + messageRepository.findByIdRolesAllowed(message.getId()).get().getText());

			authenticate(regularUser.getEmail());
			try {
				log.info("message is " + fn.apply(message.getId()).get().getText());
				//log.info("message is " + messageRepository.findByIdRolesAllowed(message.getId()).get().getText());
			} catch (Throwable e) {
				log.error("Could not obtain the result for user " + regularUser.getEmail());
			}
		}

		/*
		Set up db records and login a particular user to illustrate various security constructs within Spring Framework.
		 */
		public void run(ApplicationArguments args) throws Exception {

			//install some data
			Authority user = this.authorityRepository.save(new Authority("USER"));
			Authority admin = this.authorityRepository.save(new Authority("ADMIN"));
			User jon = this.userRepository.save(new User("jonathan@acme.com", "jon_password", admin, user));
			User bob = this.userRepository.save(new User("bob@acmecom", "bob_password", user));

			Message messageForJon = this.messageRepository.save(new Message("Hi Admin!", jon));
			this.messageRepository.save(new Message("Hi Jon!", jon));
			this.messageRepository.save(new Message("How are you?", jon));
			this.messageRepository.save(new Message("Hi Bob!", bob));

			log.info("bob: " + bob.toString());
			log.info("jon: " + jon.toString());

			//Secured by JSR250 RolesAllowed Annotation
			attemptAccess(jon, bob, messageForJon, id -> messageRepository.findByIdRolesAllowed(id));

			//Secured by Spring Secured Annotation
			attemptAccess(jon, bob, messageForJon, id -> messageRepository.findByIdSecured(id));

			//Secured by Spring PreAuthorize annotation which allows more granular SpEL syntax
			attemptAccess(jon, bob, messageForJon, id -> messageRepository.findByIdPreAuthorize(id));

			//Secured by Spring PostAuthorize annotation which allow for authorization once object has been returned.
			//attemptAccess(jon, bob, messageForJon, id -> messageRepository.findByIdBeanCheck(id));

			//Secured by Spring Data Pagination and SpEL which has access to the authenticated principal.
			authenticate(jon.getEmail());
			log.info("Getting messages for jon");
			this.messageRepository.findMessagesFor(PageRequest.of(0, 1000)).forEach(log::info);

			log.info("Getting messages for bob");
			authenticate(bob.getEmail());
			this.messageRepository.findMessagesFor(PageRequest.of(0,1000)).forEach(log::info);

			//create a message whilst Bob is logged in
			log.info("audited message: " + this.messageRepository.save(new Message("audited message", bob)));
		}




	}
}
