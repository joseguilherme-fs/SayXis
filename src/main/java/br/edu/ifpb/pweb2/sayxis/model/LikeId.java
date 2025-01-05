package br.edu.ifpb.pweb2.sayxis.model;

import jakarta.persistence.Embeddable;
import lombok.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Embeddable
@Component
@NoArgsConstructor
@AllArgsConstructor

public class LikeId implements Serializable {
    private Integer photoId;
    private Integer photographerId;

}
