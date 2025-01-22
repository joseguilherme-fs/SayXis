package br.edu.ifpb.pweb2.sayxis.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "photographer_id")
    private Photographer photographer;

    @ManyToOne
    @JoinColumn(name = "photo_id")
    private Photo photo;

    @Column(length = 512)
    private String commentText;

    private Boolean isCaption = false;

    private LocalDate createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }

    public String getFormattedCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return createdAt.format(formatter);
    }

}