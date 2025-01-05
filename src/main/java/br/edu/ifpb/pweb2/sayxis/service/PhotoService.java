package br.edu.ifpb.pweb2.sayxis.service;

import br.edu.ifpb.pweb2.sayxis.model.Photo;
import br.edu.ifpb.pweb2.sayxis.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PhotoService implements Service<Photo, Integer>{

    @Autowired
    private PhotoRepository photoRepository;

    @Override
    public List<Photo> findAll() {
        return photoRepository.findAll();
    }

    @Override
    public Photo findById(Integer integer) {
        return null;
    }

    @Override
    public Photo save(Photo photo) {
        return photoRepository.save(photo);
    }
}
