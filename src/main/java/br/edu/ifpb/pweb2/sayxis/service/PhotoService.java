package br.edu.ifpb.pweb2.sayxis.service;

import br.edu.ifpb.pweb2.sayxis.model.Photo;
import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import br.edu.ifpb.pweb2.sayxis.model.dto.PhotoDTO;
import br.edu.ifpb.pweb2.sayxis.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    public Photo findById(Integer photo_id) {
        return photoRepository.findById(photo_id)
                .orElseThrow(() -> new RuntimeException("Foto não encontrada."));
    }

    public Integer authorId(Integer photo_id) {
        Photo photo = findById(photo_id);
        return photo.getPhotographer().getId();
    }

    public Photo findProfilePhoto(Photographer photographer) {
        return photoRepository.findProfilePhoto(photographer);
    }

    public PhotoDTO addPhoto(Photographer photographer, byte[] imageData, boolean is_profilePhoto) {
        Photo photo = Photo.builder()
                .photographer(photographer)
                .is_profilePhoto(is_profilePhoto)
                .imageData(imageData)
                .build();
        return new PhotoDTO(save(photo));
    }
    public Long countPhotosByPhotographerId (Integer photographerId) {
        return photoRepository.countPhotosByPhotographerId(photographerId);
    }

    public List<Photo> findPhotosByPhotographerId(Integer photographerId) {
        return photoRepository.findPhotosByPhotographer(photographerId);
    }

    public Page<Photo> findAll(Pageable pageable) {return photoRepository.findAllFeed(pageable);
    }

    public Photo save(Photo photo) {
        return photoRepository.save(photo);
    }
}
