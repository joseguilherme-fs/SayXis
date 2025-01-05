package br.edu.ifpb.pweb2.sayxis.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Entity
@Component
public class Tag {
    @Id
    private Integer id;
    private String tagName;
}
