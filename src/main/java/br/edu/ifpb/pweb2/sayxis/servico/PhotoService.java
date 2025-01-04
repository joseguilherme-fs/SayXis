package br.edu.ifpb.pweb2.sayxis.servico;

import br.edu.ifpb.pweb2.sayxis.modelo.Photo;
import br.edu.ifpb.pweb2.sayxis.repositorio.PhotoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PhotoService implements Service<Photo, Integer>{

    @Autowired
    private PhotoRepositorio photoRepositorio;

    @Override
    public List<Photo> findAll() {
        return photoRepositorio.findAll();
    }

    @Override
    public Photo findById(Integer integer) {
        return null;
    }

    @Override
    public Photo save(Photo photo) {
        return photoRepositorio.save(photo);
    }
}
