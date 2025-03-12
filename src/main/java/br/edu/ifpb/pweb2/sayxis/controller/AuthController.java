package br.edu.ifpb.pweb2.sayxis.controller;

import br.edu.ifpb.pweb2.sayxis.model.Photo;
import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import br.edu.ifpb.pweb2.sayxis.model.dto.PhotoDTO;
import br.edu.ifpb.pweb2.sayxis.service.PhotoService;
import br.edu.ifpb.pweb2.sayxis.service.PhotographerService;
import br.edu.ifpb.pweb2.sayxis.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private PhotographerService photographerService;

    @Autowired
    private PhotoService photoService;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public ModelAndView getLoginForm(ModelAndView modelAndView) {
        modelAndView.setViewName("auth/login");
        return modelAndView;
    }

    @RequestMapping("/signup")
    public ModelAndView getSingupForm(ModelAndView modelAndView) {
        modelAndView.setViewName("register-form");
        modelAndView.addObject("photographer", new Photographer());
        return modelAndView;
    }

    @PostMapping("/signup/save")
    public ModelAndView savePhotographer(@Valid Photographer photographer, BindingResult validation,
                                         RedirectAttributes attr, @RequestParam("profilePhoto") MultipartFile file) {
        ModelAndView modelAndView = new ModelAndView();
        if (validation.hasErrors()) {
            modelAndView.setViewName("register-form");
            return modelAndView;
        }

        userService.save(photographer.getUser());
        Photographer newPhotographer = photographerService.save(photographer);

        try {
            if (!file.isEmpty()) {
                newPhotographer.setProfile_photo(file.getBytes());
                PhotoDTO savedPhoto = photoService.addPhoto(newPhotographer, file.getBytes(), true);
                Photo photo = photoService.findById(savedPhoto.getId());
                photo.setImageUrl("http://localhost:8080/photo/" + photo.getId() + "/image");
                photoService.save(photo);
            }

            attr.addFlashAttribute("message", "Fotógrafo cadastrado com sucesso!");
            modelAndView.setViewName("redirect:/auth/login");
        } catch (Exception e){
            modelAndView.addObject("mensagem", "Erro ao cadastrar fotógrafo: " + e.getMessage());
        }
        return modelAndView;
    }
}
