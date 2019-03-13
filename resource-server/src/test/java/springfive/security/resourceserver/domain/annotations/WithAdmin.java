package springfive.security.resourceserver.domain.annotations;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@WithMockUser(username = "admin", roles = "ADMIN")
@Retention(RetentionPolicy.RUNTIME)
public @interface WithAdmin {
}
