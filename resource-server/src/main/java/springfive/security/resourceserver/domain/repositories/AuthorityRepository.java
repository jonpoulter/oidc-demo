package springfive.security.resourceserver.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import springfive.security.resourceserver.domain.vo.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
