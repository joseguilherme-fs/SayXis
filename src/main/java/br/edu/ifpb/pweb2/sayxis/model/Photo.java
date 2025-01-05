package br.edu.ifpb.pweb2.sayxis.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
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