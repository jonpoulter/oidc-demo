package springfive.security.resourceserver.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Message {

    @Id
    @GeneratedValue
    private Long id;

    private String text;

    @OneToOne
    private User to;

    //Auditing Entity Listener will create these properties.
    @CreatedBy
    private String createdBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date created;


    public Message(String text, User to) {
        this.text = text;
        this.to = to;
    }
}
