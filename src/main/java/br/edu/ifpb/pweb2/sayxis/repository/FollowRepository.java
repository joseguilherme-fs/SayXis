package br.edu.ifpb.pweb2.sayxis.repository;

import br.edu.ifpb.pweb2.sayxis.model.Follow;
import br.edu.ifpb.pweb2.sayxis.model.FollowId;
import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, FollowId> {

    // Conta a quantidade de seguidores de um fotógrafo
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.followee.id = :photographerId")
    int countFollowers(@Param("photographerId") Integer photographerId);

    // Conta a quantidade de seguidores que um fotógrafo segue
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower.id = :photographerId")
    int countFollowing(@Param("photographerId") Integer photographerId);

    // Verifica se o fotógrafo já foi seguido pelo outro
    @Query("SELECT f FROM Follow f WHERE f.followee.id = :followeeId AND f.follower.id = :followerId")
    Optional<Follow> findByFolloweeIdAndFollowerId(@Param("followeeId") Integer followeeId, @Param("followerId") Integer followerId);

    default boolean isFollowed(Integer followeeId, Integer followerId) {
        return findByFolloweeIdAndFollowerId(followeeId, followerId).isPresent();
    }
}