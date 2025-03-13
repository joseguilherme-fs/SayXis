package br.edu.ifpb.pweb2.sayxis.controller;

import br.edu.ifpb.pweb2.sayxis.model.Photo;
import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import br.edu.ifpb.pweb2.sayxis.service.CommentService;
import br.edu.ifpb.pweb2.sayxis.service.LikeService;
import br.edu.ifpb.pweb2.sayxis.service.PhotoService;
import br.edu.ifpb.pweb2.sayxis.service.PhotographerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/photographer")
public class PhotographerController {

    @Autowired
    PhotographerService photographerService;

    @Autowired
    PhotoService photoService;

    @Autowired
    LikeService likeService;

    @Autowired
    CommentService commentService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // retorna o perfil do usuário
    @GetMapping("/{photographerId}/profile")
    public String viewProfile(@PathVariable("photographerId") Integer id,
                              Model model, Principal principal) {

        Photographer photographerLogged = photographerService.findByUsername(principal.getName());

        if (photographerLogged == null) {
            return "redirect:/photographer/login";
        }
        Photographer photographerDB = photographerService.findById(id);
        if (photographerDB == null) {
            return "redirect:/error";
        }

        String nickname = "@" + photographerDB.getName().toLowerCase().replace(" ", "");

        // Verificar se o fotógrafo logado está visualizando o próprio perfil
        boolean isOwnProfile = photographerLogged.getId().equals(photographerDB.getId());

        List<Photo> photos = photoService.findPhotosByPhotographerId(id);
        photos.sort((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()));

        List<Map<String, Object>> photoData = new ArrayList<>();

        for (Photo photo : photos) {
            Map<String, Object> data = new HashMap<>();
            data.put("photo", photo);
            data.put("likes", likeService.countLikes(photo.getId()));
            data.put("comments", commentService.countComments(photo.getId()));
            photoData.add(data);
        }

        model.addAttribute("photographer", photographerDB);
        model.addAttribute("profilePhoto", photoService.findProfilePhoto(photographerDB));
        model.addAttribute("nickname", nickname);
        model.addAttribute("qntFollowers", photographerService.getFollowersCount(id));
        model.addAttribute("qntFollowing", photographerService.getFollowingCount(id));
        model.addAttribute("qntPosts", photoService.countPhotosByPhotographerId(id));
        model.addAttribute("isOwnProfile", isOwnProfile);
        model.addAttribute("followAllowed", photographerDB.isFollowAllowed());
        model.addAttribute("isFollowing", photographerService.isFollowing(photographerDB, photographerLogged));
        model.addAttribute("photos", photos);
        model.addAttribute("photoData", photoData);

        return "profile";
    }

    @PostMapping("/{photographerId}/allow-followers")
    public String allowFollowers(@PathVariable("photographerId") Integer photographerId,
                                 Principal principal) {
        Photographer photographerLogged = photographerService.findByUsername(principal.getName());

        if (photographerLogged  == null) {
            return "redirect:/photographer/login";
        }

        Photographer photographerDB = photographerService.findById(photographerId);

        if (photographerDB == null) {
            return "redirect:/photographer/register";
        }

        if (photographerLogged.getId().equals(photographerId)) {
            photographerService.followAllowed(photographerDB);
        }
        return "redirect:/photographer/" + photographerId + "/profile";
    }

    @PostMapping("/{photographerId}/follow")
    public String follow(@PathVariable("photographerId") Integer photographerId,
                        Principal principal) {
        Photographer photographerLogged = photographerService.findByUsername(principal.getName());
        if (photographerLogged == null) {
            return "redirect:/photographer/login";
        }
        try {
            photographerService.follow(photographerId, photographerLogged.getId());
        } catch (IllegalStateException e) {
            // Trate o caso em que followAllowed é falso
        }
        return "redirect:/photographer/" + photographerId + "/profile";
    }

    @PostMapping("/{photographerId}/unfollow")
    public String unfollow(@PathVariable("photographerId") Integer photographerId,
                           Principal principal) {
        Photographer photographerLogged = photographerService.findByUsername(principal.getName());
        if (photographerLogged == null) {
            return "redirect:/photographer/login";
        }
        photographerService.unfollow(photographerId, photographerLogged.getId());
        return "redirect:/photographer/" + photographerId + "/profile";
    }

    @GetMapping("/suspended")
    public String getSuspendedPage() {
        return "/suspended";
    }


}
