package br.edu.ifpb.pweb2.sayxis.repository;

import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotographerRepository extends JpaRepository<Photographer, Integer> {

}