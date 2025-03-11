package br.edu.ifpb.pweb2.sayxis.service;

import br.edu.ifpb.pweb2.sayxis.model.Follow;
import br.edu.ifpb.pweb2.sayxis.model.FollowId;
import br.edu.ifpb.pweb2.sayxis.model.LikeId;
import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import br.edu.ifpb.pweb2.sayxis.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowService {

    @Autowired
    private FollowRepository repository;

    public Follow findById(FollowId follow_id) {
        return repository.findById(follow_id)
                .orElseThrow(() -> new RuntimeException("Follow não encontrado."));
    }

    public Follow save(Follow follow) {
        return repository.save(follow);
    }

    // conta a quantidade de seguidores de um fotógrafo
    public Integer countFollowers(Integer followeeId) {
        return repository.countFollowers(followeeId);
    }

    // conta a quantidade de seguidores que um fotógrafo segue
    public Integer countFollowing(Integer followerId) {
        return repository.countFollowing(followerId);
    }



    // está seguido por?
    public Boolean isFollowed(Integer followeeId, Integer followerId) {
        return repository.isFollowed(followeeId, followerId);
    }

    public void addFollow(Photographer followee, Photographer follower) {
        FollowId followId = new FollowId(followee.getId(), follower.getId());
        Follow follow = new Follow(followId, followee, follower);
        repository.save(follow);
    }

    public void removeFollow(Integer followee_id, Integer follower_id) {
        FollowId followId = new FollowId(followee_id, follower_id);
        if (findById(followId ) != null) {
            repository.deleteById(followId);
        }
    }

}





