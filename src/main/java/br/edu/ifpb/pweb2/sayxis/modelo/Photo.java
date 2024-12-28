package br.edu.ifpb.pweb2.sayxis.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Data
@Entity
@Component
public class Photo {
    @Id
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "photographer_id")
    private Photographer photographer;
    private String imageUrl;
    private byte[] imageData;
}