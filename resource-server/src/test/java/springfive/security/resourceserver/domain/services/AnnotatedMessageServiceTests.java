package springfive.security.resourceserver.domain.services;

import static org.assertj.core.api.Assertions.assertThatCode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import springfive.security.resourceserver.domain.annotations.WithAdmin;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AnnotatedMessageServiceTests {

    @Autowired
    MessageService messageService;


    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void failedDueToNotAuthenticated() {
        this.messageService.getMessage(1L);
    }

    @Test
    @WithMockUser
    public void notAuthorized() {
        assertThatCode(() -> this.messageService.getMessage(1L))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithMockUser(roles= "ADMIN")
    public void authorized() {
        assertThatCode(() -> this.messageService.getMessage(1L))
                .doesNotThrowAnyException();
    }

    @Test
    //Our own handrolled annotation - ideal for testing for different personas.
    @WithAdmin
    public void authorizedWithAdmin() {
        assertThatCode(() -> this.messageService.getMessage(1L))
                .doesNotThrowAnyException();
    }
}
