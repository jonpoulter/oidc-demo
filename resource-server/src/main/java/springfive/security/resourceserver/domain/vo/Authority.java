package springfive.security.resourceserver.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = "users")
public class Authority {

    @Id
    @GeneratedValue
    private Long id;

    private String authority;

    @ManyToMany(cascade = {
            CascadeType.PERSIST, CascadeType.MERGE
    })
    @JoinTable(
            name = "authority_user",
            joinColumns = @JoinColumn (name = "authority_id"),
            inverseJoinColumns = @JoinColumn (name = "user_id")
    )
    private List<User> users = new ArrayList<>();

    public Authority(String authority) {
        this.authority = authority;
    }

    public Authority(String authority, Set<User> users) {
        this(authority);
        this.users.addAll(users);
    }


}
