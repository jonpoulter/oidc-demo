package springfive.security.resourceserver.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
//prevent recursive graph
@EqualsAndHashCode(exclude = "authorities")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String email, password;

    //Authority has a users field.
    //One user can have multiple authorities
    //One authority can be shared across multiple users
    @ManyToMany(mappedBy = "users")
    private List<Authority> authorities = new ArrayList<>();


    public User(String email, String password, Set<Authority> authorities) {
        this(email, password);
        this.authorities.addAll(authorities);
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String email, String password, Authority... authorities) {
        this(email, password);
        this.authorities.addAll(new HashSet<>(Arrays.asList(authorities)));

    }
}
