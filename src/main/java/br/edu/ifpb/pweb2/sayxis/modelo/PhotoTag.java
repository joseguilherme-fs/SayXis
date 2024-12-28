package br.edu.ifpb.pweb2.sayxis.modelo;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Entity
@Component
public class PhotoTag {

    @EmbeddedId
    private PhotoTagId id;

    @ManyToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;
}