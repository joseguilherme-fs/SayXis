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

    public Long getFollowersCount(Integer photographerId) {
        return repository.countFollowersByPhotographerId(photographerId);
    }

    public Long getFollowingCount(Integer photographerId) {
        return repository.countFollowingByPhotographerId(photographerId);
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
        return follower.getFollowing().contains(followed);
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

        if (!follower.getFollowing().contains(followed)) {
            follower.getFollowing().add(followed);
            repository.save(follower);
        }
    }

    public void unfollow(Integer followedId, Integer followerId) {
        if (followerId.equals(followedId)) {
            throw new IllegalArgumentException("Você não pode deixar de seguir a si mesmo.");
        }
        Photographer followed = findById(followedId);
        Photographer follower = findById(followerId);

        if (follower.getFollowing().contains(followed)) {
            follower.getFollowing().remove(followed);
            repository.save(follower);
        }
    }
}
