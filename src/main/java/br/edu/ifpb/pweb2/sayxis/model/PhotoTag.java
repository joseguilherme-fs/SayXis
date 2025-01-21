package br.edu.ifpb.pweb2.sayxis.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Entity
@Component
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhotoTag {

    @EmbeddedId
    private PhotoTagId id;

    @ManyToOne
    @JoinColumn(name = "photo_id", insertable = false, updatable = false)
    private Photo photo;

    @ManyToOne
    @JoinColumn(name = "tag_id", insertable = false, updatable = false)
    private Tag tag;
}