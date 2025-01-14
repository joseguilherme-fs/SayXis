package br.edu.ifpb.pweb2.sayxis.repository;

import br.edu.ifpb.pweb2.sayxis.model.Like;
import br.edu.ifpb.pweb2.sayxis.model.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface LikeRepository extends JpaRepository<Like, LikeId> {

    @Query("select count(l) from Like l where l.photo = :photo_id")
    int countLikes(@Param("photo_id") Integer photo_id);

    @Query("select count(l) > 0 from Like l where l.photographer = :photographer_id and l.photo = :photo_id")
    boolean isLiked(@Param("photographer_id") Integer photographer_id, @Param("photo_id") Integer photo_id);
}
