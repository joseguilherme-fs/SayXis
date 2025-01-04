package br.edu.ifpb.pweb2.sayxis.repositorio;

import br.edu.ifpb.pweb2.sayxis.modelo.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepositorio extends JpaRepository<Photo, Integer> {

}
