package br.edu.ifpb.pweb2.sayxis.controller;

import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import br.edu.ifpb.pweb2.sayxis.model.Photo;
import br.edu.ifpb.pweb2.sayxis.repository.PhotographerRepository;
import br.edu.ifpb.pweb2.sayxis.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
public class AdminController {

    @Autowired
    PhotographerRepository photographerService;

    @Autowired
    private PhotoService photoService;

    @GetMapping("/admin")
    public String adminPage() {
        return "/admin";
    }

    @GetMapping("/admin/access-denied")
    public String accessDeniedPage() {
        return "/access-denied";
    }

    @GetMapping("/admin/list-users")
    public String listUsers(Model model) {
        model.addAttribute("photographers", photographerService.findAll());
        model.addAttribute("listPhotographers", true);
        return "/admin";
    }

    @GetMapping("/admin/{photographer_id}/edit")
    public String editPhotographer(Model model, @PathVariable Integer photographer_id) {
        Photographer photographer = photographerService.findById(photographer_id).get();

        model.addAttribute("photographer", photographer);
        Photo profilePhoto = photoService.findProfilePhoto(photographer);
        if(profilePhoto != null) {
            model.addAttribute("profilePhoto", profilePhoto);
        }
        return "/photographer-page";
    }

    @PostMapping("/admin/{photographer_id}/suspend")
    public String suspendPhotographer(RedirectAttributes ra, @PathVariable Integer photographer_id) {
        Photographer photographer = photographerService.findById(photographer_id).get();
        photographer.set_suspended(true);
        photographerService.save(photographer);
        ra.addFlashAttribute("photographer", photographer);

        return "redirect:/admin";
    }
}