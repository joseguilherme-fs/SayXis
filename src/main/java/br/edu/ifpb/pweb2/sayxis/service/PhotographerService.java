package br.edu.ifpb.pweb2.sayxis.service;

import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import br.edu.ifpb.pweb2.sayxis.repository.PhotographerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotographerService {

    @Autowired
    private PhotographerRepository repository;

    @Autowired
    private FollowService followService;



    public Photographer findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public Photographer save(Photographer photographer) {
        repository.save(photographer);
        return photographer;
    }

    public Photographer findByEmail(String email){
        return repository.findByEmail(email);
    }

    public List<Photographer> findSuspendeds() {
        return repository.findSuspendeds();
    }

    public Integer getFollowersCount(Integer photographerId) {
        return followService.countFollowers(photographerId);
    }

    public Integer getFollowingCount(Integer photographerId) {
        return followService.countFollowing(photographerId);
    }

    public void followAllowed(Photographer photographer){
        if(photographer.isFollowAllowed()) {
            photographer.setFollowAllowed(false);
        } else {
            photographer.setFollowAllowed(true);
        }
        repository.save(photographer);
    }

    public boolean isFollowing(Photographer followed, Photographer follower) {
        return followService.isFollowed(followed.getId(), follower.getId());
    }

    public void follow(Integer followedId, Integer followerId) {
        if (followerId.equals(followedId)) {
            throw new IllegalArgumentException("Você não pode seguir a si mesmo.");
        }
        Photographer followed = findById(followedId);
        Photographer follower = findById(followerId);

        if (!followed.isFollowAllowed()) {
            throw new IllegalStateException("Este fotógrafo não permite ser seguido no momento.");
        }

        if (isFollowing(followed, follower)) {
            throw new IllegalArgumentException("Você já está seguindo este usuário.");
        }
        followService.addFollow(followed, follower);
    }

    public void unfollow(Integer followedId, Integer followerId) {
        if (followerId.equals(followedId)) {
            throw new IllegalArgumentException("Você não pode deixar de seguir a si mesmo.");
        }
        Photographer followed = findById(followedId);
        Photographer follower = findById(followerId);

        if (isFollowing(followed, follower)) {
            followService.removeFollow(followedId,followerId);
        }
    }
}
