package springfive.security.resourceserver.domain.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class AuditConfig {

    /**
     * AuditorAware implementation which is enabled by using the @EnableJpaAuditing
     * @return
     */
    @Bean
    AuditorAware<String> auditor() {

        return new AuditorAware<String>() {
            @Override
            public Optional<String> getCurrentAuditor() {
                SecurityContext context = SecurityContextHolder.getContext();
                Authentication authentication = context.getAuthentication();

                if (null != authentication) {
                    return Optional.ofNullable(authentication.getName());
                }
                return Optional.empty();
            }
        };
    }
}
