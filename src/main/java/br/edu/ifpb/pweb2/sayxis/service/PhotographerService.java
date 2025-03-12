package br.edu.ifpb.pweb2.sayxis.service;

import br.edu.ifpb.pweb2.sayxis.model.Authority;
import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import br.edu.ifpb.pweb2.sayxis.model.User;
import br.edu.ifpb.pweb2.sayxis.model.dto.PhotographerDTO;
import br.edu.ifpb.pweb2.sayxis.repository.PhotographerRepository;
import br.edu.ifpb.pweb2.sayxis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PhotographerService {

    @Autowired
    private PhotographerRepository repository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private FollowService followService;

    @Autowired
    private PasswordEncoder passwordEncoder;



    public Photographer findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public Photographer save(Photographer photographer) {
        Optional<Photographer> existingPhotographer = repository.findByEmail(photographer.getEmail());
        if (existingPhotographer.isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado!");
        }
        return repository.save(photographer);
    }

    public Photographer findByUsername(String username) {
        return repository.findByUsername(username);
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
