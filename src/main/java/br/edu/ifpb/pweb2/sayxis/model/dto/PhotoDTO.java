package br.edu.ifpb.pweb2.sayxis.model.dto;

import br.edu.ifpb.pweb2.sayxis.model.Photo;
import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import lombok.Data;

@Data
public class PhotoDTO {
    private Integer id;
    private Photographer photographer;

    public PhotoDTO(Photo photo) {
        this.id = photo.getId();
        this.photographer = photo.getPhotographer();
    }

}
