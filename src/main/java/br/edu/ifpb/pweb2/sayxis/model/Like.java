package br.edu.ifpb.pweb2.sayxis.model;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

@Data
@Entity
@Component
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "likes")
public class Like {

    @EmbeddedId
    private LikeId id;

    @ManyToOne
    @JoinColumn(name = "photographer_id", insertable = false, updatable = false)
    private Photographer photographer;

    @ManyToOne
    @JoinColumn(name = "photo_id", insertable = false, updatable = false)
    private Photo photo;

}
