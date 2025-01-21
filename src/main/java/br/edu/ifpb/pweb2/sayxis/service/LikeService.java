package br.edu.ifpb.pweb2.sayxis.service;

import br.edu.ifpb.pweb2.sayxis.model.Like;
import br.edu.ifpb.pweb2.sayxis.model.LikeId;
import br.edu.ifpb.pweb2.sayxis.model.Photo;
import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import br.edu.ifpb.pweb2.sayxis.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService{

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private PhotographerService photographerService;

    public List<Like> findAll() {
        return likeRepository.findAll();
    }

    public Like findById(LikeId likeId) {
        Like like = null;
        Optional<Like> likeOptional = likeRepository.findById(likeId);
        if(likeOptional.isPresent()){
            like = likeOptional.get();
        }
        return like;
    }

    public Like save(Like like) {
        return likeRepository.save(like);
    }

    public Integer countLikes(Integer photoId) {
        Photo photo = photoService.findById(photoId);
        if (photo != null) {
            return likeRepository.countLikes(photo);
        } else {
            return null;
        }
    }

    public Boolean isLiked(Integer photographerId, Integer photoId) {
        Photographer photographer = photographerService.findById(photographerId);
        Photo photo = photoService.findById(photoId);
        if (photo != null) {
            return likeRepository.isLiked(photographer, photo);
        } else {
            return null;
        }
    }

}
