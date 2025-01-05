package br.edu.ifpb.pweb2.sayxis.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Comment {
    @Id
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "photographer_id")
    private Photographer photographer;
    @ManyToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;
    private String commentText;
    private LocalDateTime createdAt;
}