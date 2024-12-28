package br.edu.ifpb.pweb2.sayxis.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Data
@Entity
@Component
public class Photographer {
    @Id
    private Integer id;
    private String name;
    private String email;
}