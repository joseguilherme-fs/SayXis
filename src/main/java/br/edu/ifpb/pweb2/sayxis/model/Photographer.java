package br.edu.ifpb.pweb2.sayxis.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Photographer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private boolean is_adm = false;
    private boolean is_suspended = false;
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean followAllowed = true;
    private byte[] profile_photo = null;
    private String city = null;
    private String country = null;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "followers", // Nome da tabela de junção
            joinColumns = @JoinColumn(name = "photographer_id"), // Chave estrangeira para este fotógrafo
            inverseJoinColumns = @JoinColumn(name = "follower_id") // Chave estrangeira para o seguidor
    )
    private List<Photographer> following;

    // Lista de fotógrafos que seguem este fotógrafo
    @ManyToMany(mappedBy = "following")
    private List<Photographer> followers;
}