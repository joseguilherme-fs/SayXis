package br.edu.ifpb.pweb2.sayxis.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Embeddable
@Component
public class PhotoTagId implements Serializable {
    private Integer photoId;
    private Integer tagId;
}
