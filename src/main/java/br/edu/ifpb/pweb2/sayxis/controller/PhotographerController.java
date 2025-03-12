package br.edu.ifpb.pweb2.sayxis.controller;

import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import br.edu.ifpb.pweb2.sayxis.model.dto.PhotographerDTO;
import br.edu.ifpb.pweb2.sayxis.service.PhotoService;
import br.edu.ifpb.pweb2.sayxis.service.PhotographerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/photographer")
public class PhotographerController {

    @Autowired
    PhotographerService photographerService;

    @Autowired
    PhotoService photoService;

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

        // Adicionar atributos ao modelo
        model.addAttribute("photographer", photographerDB);
        model.addAttribute("profilePhoto", photoService.findProfilePhoto(photographerDB));
        model.addAttribute("nickname", nickname);
        model.addAttribute("qntFollowers", photographerService.getFollowersCount(id));
        model.addAttribute("qntFollowing", photographerService.getFollowingCount(id));
        model.addAttribute("qntPosts", photoService.countPhotosByPhotographerId(id));
        model.addAttribute("isOwnProfile", isOwnProfile);
        model.addAttribute("followAllowed", photographerDB.isFollowAllowed());
        model.addAttribute("isFollowing", photographerService.isFollowing(photographerDB, photographerLogged));
        model.addAttribute("photos", photoService.findPhotosByPhotographerId(id));

        return "profile";
    }

    @GetMapping("/my/profile")
    public String viewMyProfile(
            Model model, Principal principal) {

        Photographer photographerLogged = photographerService.findByUsername(principal.getName());

        if (photographerLogged == null) {
            return "redirect:/photographer/login";
        }

        String nickname = "@" + photographerLogged.getName().toLowerCase().replace(" ", "");

        Boolean isOwnProfile = true;

        // Adicionar atributos ao modelo
        model.addAttribute("photographer", photographerLogged);
        model.addAttribute("profilePhoto", photoService.findProfilePhoto(photographerLogged));
        model.addAttribute("nickname", nickname);
        model.addAttribute("qntFollowers", photographerService.getFollowersCount(photographerLogged.getId()));
        model.addAttribute("qntFollowing", photographerService.getFollowingCount(photographerLogged.getId()));
        model.addAttribute("qntPosts", photoService.countPhotosByPhotographerId(photographerLogged.getId()));
        model.addAttribute("isOwnProfile", isOwnProfile);
        model.addAttribute("followAllowed", photographerLogged.isFollowAllowed());
        model.addAttribute("isFollowing", photographerService.isFollowing(photographerLogged, photographerLogged));
        model.addAttribute("photos", photoService.findPhotosByPhotographerId(photographerLogged.getId()));

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

    // seguir um fotógrafo
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
