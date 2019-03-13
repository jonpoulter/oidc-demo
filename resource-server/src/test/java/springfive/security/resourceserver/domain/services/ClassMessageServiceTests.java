package springfive.security.resourceserver.domain.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit4.SpringRunner;
import springfive.security.resourceserver.domain.annotations.WithAdmin;

@RunWith(SpringRunner.class)
@SpringBootTest
@WithAdmin
public class ClassMessageServiceTests {

    @Autowired
    MessageService messageService;

    @Test
    public void a() {
        this.messageService.getMessage(1L);
    }

    @Test
    public void b() {
        this.messageService.getMessage(1L);
    }

    @Test(expected = AccessDeniedException.class)
    @WithAnonymousUser
    public void c() {
        this.messageService.getMessage(1L);
    }
}
