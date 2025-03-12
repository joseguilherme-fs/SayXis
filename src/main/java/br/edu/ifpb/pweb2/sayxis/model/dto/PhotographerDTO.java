package br.edu.ifpb.pweb2.sayxis.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PhotographerDTO {
    private String name;
    private String username;
    private String email;
    private String password;
    private String city;
    private byte[] profile_photo;
    private String country;
}

