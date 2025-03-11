package br.edu.ifpb.pweb2.sayxis.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class FollowId implements Serializable {
    private Integer followee_id;  // aquele que é seguido
    private Integer follower_id; // aquele que segue

}
