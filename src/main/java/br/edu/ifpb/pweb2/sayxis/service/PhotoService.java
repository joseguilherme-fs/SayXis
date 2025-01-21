package br.edu.ifpb.pweb2.sayxis.service;

import br.edu.ifpb.pweb2.sayxis.model.Comment;
import br.edu.ifpb.pweb2.sayxis.model.Photo;
import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import br.edu.ifpb.pweb2.sayxis.model.dto.PhotoDTO;
import br.edu.ifpb.pweb2.sayxis.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    public List<Photo> findAll() {
        return photoRepository.findAll();
    }

    public Photo findById(Integer photo_id) {
        return photoRepository.findById(photo_id)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada."));
    }


    public PhotoDTO addPhoto(Photographer photographer, byte[] imageData) {
        Photo photo = Photo.builder()
                .photographer(photographer)
                .imageData(imageData)
                .build();
        return new PhotoDTO(save(photo));
    }

    public Photo save(Photo photo) {
        return photoRepository.save(photo);
    }

    public Comment getCaption(List<Comment> comments) {
        Comment caption = null;
        Optional<Comment> photoCaption = comments.stream()
                .filter(Comment::getIsCaption)
                .findFirst();
        if (photoCaption.isPresent()) {
            caption = photoCaption.get();
        }
        return caption;
    }
}
