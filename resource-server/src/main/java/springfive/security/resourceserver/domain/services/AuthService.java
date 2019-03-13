package springfive.security.resourceserver.domain.services;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import springfive.security.resourceserver.domain.vo.Message;
import springfive.security.resourceserver.domain.vo.User;

import java.util.Optional;


//This bean is used to verify @PostAuthorize custom SpEL validation check in MessageRepository.
@Log4j2
@Service("authz")
public class AuthService {

    public boolean check(Optional<Message> msg, User user) {
        log.info("checking " + user.getEmail());
        return msg.get().getTo().getId().equals(user.getId());
    }

    public boolean check(Optional<Message> msg, String email) {

        if (!msg.isPresent()) {
            return true;
        }
        return msg.get().getTo().getEmail().equals(email);
    }
}
