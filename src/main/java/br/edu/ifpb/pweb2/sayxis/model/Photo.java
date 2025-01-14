package br.edu.ifpb.pweb2.sayxis.model;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "photographer_id")
    private Photographer photographer;
    private String imageUrl;
    private byte[] imageData;
}