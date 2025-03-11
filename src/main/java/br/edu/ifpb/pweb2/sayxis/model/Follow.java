package br.edu.ifpb.pweb2.sayxis.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Entity
@Component
@NoArgsConstructor
@AllArgsConstructor
public class Follow {

    @EmbeddedId
    private FollowId id;

    @ManyToOne
    @JoinColumn(name = "followee_id", insertable = false, updatable = false)
    private Photographer followee; // aquele que é seguido
    @ManyToOne
    @JoinColumn(name = "follower_id", insertable = false, updatable = false)
    private Photographer follower; // aquele que segue
}
