package br.edu.ifpb.pweb2.sayxis.repository;

import br.edu.ifpb.pweb2.sayxis.model.Like;
import br.edu.ifpb.pweb2.sayxis.model.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface LikeRepository extends JpaRepository<Like, LikeId> {

    @Query("select count(l) from Like l where l.photo = :photoId")
    int countLikes(@Param("photoId") Integer photoId);

    @Query("select count(l) > 0 from Like l where l.photographer = :photographerId and l.photo = :photoId")
    boolean isLiked(@Param("photographerId") Integer photographerId, @Param("photoId") Integer photoId);
}
