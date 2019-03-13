package springfive.security.resourceserver.domain.services;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import springfive.security.resourceserver.domain.repositories.MessageRepository;
import springfive.security.resourceserver.domain.vo.Message;

import java.util.Optional;

/* Must be an admin to access these methods */
@Service
@PreAuthorize("hasRole('ADMIN')")
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    public Optional<Message> getMessage(Long id) {
        return messageRepository.findById(id);
    }

    public Optional<Message> getMyMessage(Long id) {
        return messageRepository.findByIdBeanCheck(id);
    }

    //@PostAuthorize("returnObject?.to?.id == principal?.id")
    @PostAuthorize("@authz.check(returnObject, principal?.user)")
    public Optional<Message> getMessageCheckBean(Long id) {
        return Optional.empty();
    }
}
