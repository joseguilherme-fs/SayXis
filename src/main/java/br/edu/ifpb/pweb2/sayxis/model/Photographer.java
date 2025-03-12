package br.edu.ifpb.pweb2.sayxis.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Photographer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "O nome é obrigatório!")
    private String name;

    @Valid
    @OneToOne
    @JoinColumn(name = "username")
    private User user;

    @NotBlank(message = "O e-mail é obrigatório")
    @Column(unique = true)
    private String email;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean is_adm = false;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean is_suspended = false;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean followAllowed = true;

    private byte[] profile_photo = null;

    @NotBlank(message = "A cidade é obrigatória")
    private String city = null;

    @NotBlank(message = "O país é obrigatório")
    private String country = null;
}
