package br.edu.ifpb.pweb2.sayxis.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Data
@Entity
@Component
public class Tag {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String tagName;

    public Integer getId() {
        return id;
    }
}
