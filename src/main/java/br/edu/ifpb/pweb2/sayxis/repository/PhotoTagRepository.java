package br.edu.ifpb.pweb2.sayxis.repository;

import br.edu.ifpb.pweb2.sayxis.model.PhotoTag;
import br.edu.ifpb.pweb2.sayxis.model.PhotoTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoTagRepository extends JpaRepository<PhotoTag, PhotoTagId> {
}
