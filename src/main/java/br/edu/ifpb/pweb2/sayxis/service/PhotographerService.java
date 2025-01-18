package br.edu.ifpb.pweb2.sayxis.service;

import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import br.edu.ifpb.pweb2.sayxis.repository.PhotographerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PhotographerService implements Service<Photographer, Integer> {

    @Autowired
    private PhotographerRepository photographerRepository;

    @Override
    public List<Photographer> findAll() {
        return photographerRepository.findAll();
    }

    @Override
    public Photographer findById(Integer id) {
        Photographer photographer = null;
        Optional<Photographer> opPhotographer = photographerRepository.findById(id);
        if (opPhotographer.isPresent()) {
            photographer = opPhotographer.get();
        }
        return photographer;
    }

    @Override
    public Photographer save(Photographer photographer) {
        if(findById(photographer.getId()) != null) {
            return null;
        } else {
            return photographerRepository.save(photographer);
        }
    }

    public Photographer findByEmail(String email){
        return photographerRepository.findByEmail(email);
    }

    public List<Photographer> findSuspendeds() {
        return photographerRepository.findSuspendeds();
    }
}
