package br.edu.ifpb.pweb2.sayxis.repository;

import br.edu.ifpb.pweb2.sayxis.model.Photo;
import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {

    @Query("select p from Photo p where p.is_profilePhoto = false")
    Page<Photo> findAllFeed(Pageable pageable);

    Optional<Photo> findById(Integer id);

    @Query("SELECT COUNT(p) FROM Photo p WHERE p.photographer.id = :id AND p.is_profilePhoto = FALSE")
    long countPhotosByPhotographerId(@Param("id") Integer id);

    @Query("select p from Photo p where p.photographer = :photographer and p.is_profilePhoto = true")
    Photo findProfilePhoto(@Param("photographer") Photographer photographer);

    @Query("SELECT p FROM Photo p WHERE p.photographer.id = :photographerId AND p.is_profilePhoto = false")
    List<Photo> findPhotosByPhotographer(@Param("photographerId") Integer photographerId);

    // Método para buscar fotos ordenadas pela data de criação
    @Query("SELECT p FROM Photo p ORDER BY p.createdAt DESC")
    Page<Photo> findAllOrderByCreatedAtDesc(Pageable pageable);
}
