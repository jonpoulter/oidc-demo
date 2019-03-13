package springfive.security.resourceserver.domain.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import springfive.security.resourceserver.domain.vo.Message;

import javax.annotation.security.RolesAllowed;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    //Explicit Query as by default JPA will attempt to retrieve Message with column 'IdRolesAllowed'
    String QUERY = "select m from Message m where m.id = ?1";

    //JSR250 annotation need to prefixed with ROLE_
    @RolesAllowed("ROLE_ADMIN")
    Optional<Message> findById(Long id);

    @Query (QUERY)
    @RolesAllowed("ROLE_ADMIN")
    Optional<Message> findByIdRolesAllowed(Long id);

    @Query(QUERY)
    @Secured("ROLE_ADMIN")
    Optional<Message> findByIdSecured(Long id);


    @Query(QUERY)
    @PreAuthorize("hasRole('ADMIN')")
    Optional<Message> findByIdPreAuthorize(Long id);

    //See AuthService for check() implementation.
    @Query(QUERY)
    //@PostAuthorize("@authz.check(returnObject, principal?.user)")
    @PostAuthorize("@authz.check(returnObject, principal?.claims['email'])")
    Optional<Message> findByIdBeanCheck(Long id);

    //running JPAQL and access to the current authenticated principal without having to bring it into memory and filter there.
    @Query("select m from Message m where m.to.id = ?#{principal?.user?.id}")
    Page<Message> findMessagesFor(Pageable pageable);
}
