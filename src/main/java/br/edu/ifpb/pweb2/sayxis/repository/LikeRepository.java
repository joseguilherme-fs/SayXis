package br.edu.ifpb.pweb2.sayxis.repository;

import br.edu.ifpb.pweb2.sayxis.model.Like;
import br.edu.ifpb.pweb2.sayxis.model.LikeId;
import br.edu.ifpb.pweb2.sayxis.model.Photo;
import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface LikeRepository extends JpaRepository<Like, LikeId> {

    @Query("select count(l) from Like l where l.photo = :photo")
    int countLikes(@Param("photo") Photo photo);

    @Query("select count(l) > 0 from Like l where l.photographer = :photographer and l.photo = :photo")
    boolean isLiked(@Param("photographer") Photographer photographer, @Param("photo") Photo photo);
}
