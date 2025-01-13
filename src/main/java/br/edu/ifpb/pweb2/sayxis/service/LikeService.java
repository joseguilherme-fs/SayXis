package br.edu.ifpb.pweb2.sayxis.service;

import br.edu.ifpb.pweb2.sayxis.model.Like;
import br.edu.ifpb.pweb2.sayxis.repository.LikeRepository;
import br.edu.ifpb.pweb2.sayxis.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LikeService implements Service<Like, Integer>{

    @Autowired
    private LikeRepository likeRepository;

    @Override
    public List<Like> findAll() {
        return likeRepository.findAll();
    }

    @Override
    public Like findById(Integer integer) {
        return null;
    }

    @Override
    public Like save(Like like) {
        return likeRepository.save(like);
    }
}
