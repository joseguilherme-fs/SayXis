package br.edu.ifpb.pweb2.sayxis.servico;

import br.edu.ifpb.pweb2.sayxis.modelo.Photo;
import br.edu.ifpb.pweb2.sayxis.modelo.Photographer;
import br.edu.ifpb.pweb2.sayxis.repositorio.PhotographerRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PhotographerService implements Service<Photographer, Integer> {

    @Autowired
    private PhotographerRepositorio photographerRepositorio;

    @Override
    public List<Photographer> findAll() {
        return photographerRepositorio.findAll();
    }

    @Override
    public Photographer findById(Integer integer) {
        return null;
    }

    @Override
    public Photographer save(Photographer photographer) {
        return photographerRepositorio.save(photographer);
    }
}
