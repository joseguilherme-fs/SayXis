package br.edu.ifpb.pweb2.sayxis.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor

public class LikeId implements Serializable {
    private Integer photo_id;
    private Integer photographer_id;

}
