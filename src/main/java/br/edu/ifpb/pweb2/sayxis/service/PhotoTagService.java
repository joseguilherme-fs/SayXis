package br.edu.ifpb.pweb2.sayxis.service;

import br.edu.ifpb.pweb2.sayxis.model.PhotoTag;
import br.edu.ifpb.pweb2.sayxis.model.PhotoTagId;
import br.edu.ifpb.pweb2.sayxis.repository.PhotoTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoTagService {

    @Autowired
    private PhotoTagRepository photoTagRepository;

    public List<PhotoTag> findAll() {
        return photoTagRepository.findAll();
    }

    public PhotoTag findById(PhotoTagId photoTagId) {
        return null;
    }

    public PhotoTag save(PhotoTag photoTag) {
        return photoTagRepository.save(photoTag);
    }
}
