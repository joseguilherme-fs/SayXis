package br.edu.ifpb.pweb2.sayxis.service;

import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import br.edu.ifpb.pweb2.sayxis.repository.PhotographerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotographerService {

    @Autowired
    private PhotographerRepository photographerRepository;

    public List<Photographer> findAll() {
        return photographerRepository.findAll();
    }

    public Photographer findById(Integer id) {
        return photographerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fotográfo não encontrado."));
    }

    public void save(Photographer photographer) {
        photographerRepository.save(photographer);
    }

    public Photographer findByEmail(String email){
        return photographerRepository.findByEmail(email);
    }

    public List<Photographer> findSuspendeds() {
        return photographerRepository.findSuspendeds();
    }
}
