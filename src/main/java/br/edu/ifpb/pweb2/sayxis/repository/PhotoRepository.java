package br.edu.ifpb.pweb2.sayxis.repository;

import br.edu.ifpb.pweb2.sayxis.model.Photo;
import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {

    Optional<Photo> findById(Integer id);

    @Query("SELECT COUNT(p) FROM Photo p WHERE p.photographer.id = :id AND p.is_profilePhoto = FALSE")
    long countPhotosByPhotographerId(@Param("id") Integer id);

    @Query("select p from Photo p where p.photographer = :photographer and p.is_profilePhoto = true")
    Photo findProfilePhoto(@Param("photographer") Photographer photographer);
}
