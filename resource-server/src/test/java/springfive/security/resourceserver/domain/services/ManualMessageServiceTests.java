package springfive.security.resourceserver.domain.services;

import com.nimbusds.jose.proc.SecurityContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.event.AuthenticationCredentialsNotFoundEvent;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThatCode;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ManualMessageServiceTests {

    @Autowired
    MessageService messageService;

    @Before
    public void init() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void failedDueToNotAuthenticated() {
        this.messageService.getMessage(1L);
    }

    //Using AssertJ
    @Test
    public void notAuthorized() {
        TestingAuthenticationToken token = new TestingAuthenticationToken("user",
                "password",
                "ROLE_USER");

        SecurityContextHolder.getContext().setAuthentication(token);

        assertThatCode(() -> this.messageService.getMessage(1L))
                .isInstanceOf(AccessDeniedException.class);
    }


    @Test
    public void authorized() {
        TestingAuthenticationToken token = new TestingAuthenticationToken("user",
                "password",
                "ROLE_ADMIN");

        SecurityContextHolder.getContext().setAuthentication(token);

        assertThatCode(() -> this.messageService.getMessage(1L))
                .doesNotThrowAnyException();
    }

}
