package springfive.security.resourceserver.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import springfive.security.resourceserver.domain.vo.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
