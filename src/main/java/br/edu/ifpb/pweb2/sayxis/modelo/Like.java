package br.edu.ifpb.pweb2.sayxis.modelo;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

@Data
@Entity
@Component
@NoArgsConstructor
@AllArgsConstructor

public class Like {

    @EmbeddedId
    private LikeId id;

    @ManyToOne
    @JoinColumn(name = "photographer_id")
    private Photographer photographer;

    @ManyToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;

}
