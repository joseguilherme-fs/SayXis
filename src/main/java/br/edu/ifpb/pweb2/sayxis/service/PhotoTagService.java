package br.edu.ifpb.pweb2.sayxis.service;

import br.edu.ifpb.pweb2.sayxis.model.PhotoTag;
import br.edu.ifpb.pweb2.sayxis.model.PhotoTagId;
import br.edu.ifpb.pweb2.sayxis.repository.PhotoTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PhotoTagService implements Service<PhotoTag, PhotoTagId> {

    @Autowired
    private PhotoTagRepository photoTagRepository;

    @Override
    public List<PhotoTag> findAll() {
        return photoTagRepository.findAll();
    }

    @Override
    public PhotoTag findById(PhotoTagId photoTagId) {
        return null;
    }

    @Override
    public PhotoTag save(PhotoTag photoTag) {
        return photoTagRepository.save(photoTag);
    }
}
