package springfive.security.resourceserver.domain.services;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import springfive.security.resourceserver.domain.annotations.WithAdmin;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomAuthorizationTests {

    @Autowired
    MessageService messageService;


    @Test
    @WithUserDetails("Jon")
    public void wrongType() {
        this.messageService.getMessageCheckBean(1L);
    }
}
