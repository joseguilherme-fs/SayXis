package br.edu.ifpb.pweb2.sayxis.controller;

import br.edu.ifpb.pweb2.sayxis.model.Photo;
import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import br.edu.ifpb.pweb2.sayxis.model.dto.PhotoDTO;
import br.edu.ifpb.pweb2.sayxis.model.dto.PhotographerDTO;
import br.edu.ifpb.pweb2.sayxis.service.PhotoService;
import br.edu.ifpb.pweb2.sayxis.service.PhotographerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/photographer")
public class PhotographerController {

    @Autowired
    PhotographerService photographerService;

    @Autowired
    PhotoService photoService;

    //retorna a página de cadastro de fotógrafo
    @GetMapping("/register")
    public ModelAndView getForm(Photographer photographer, BindingResult validation, ModelAndView model) {
        model.addObject("photographer", photographer);
        model.setViewName("register-form");
        return model;
    }

    //cadastra um fotógrafo
    @PostMapping("/register")
    public ModelAndView registerPhotographer(@RequestParam("profilePhoto") MultipartFile file,
                                             @Valid Photographer photographer,
                                             BindingResult validation,
                                             ModelAndView model,
                                             HttpSession session,
                                             RedirectAttributes redAttrs) {
        //Verifica se tem erro no formulário
        if (validation.hasErrors()) {
            model.setViewName("register-form");
            return model;
        }
        // Verifica se os campos obrigatórios estão preenchidos
        if (photographer.getName() == null || photographer.getName().trim().isEmpty()) {
            model.addObject("mensagem", "Erro: O nome é obrigatório!");
            model.setViewName("register-form");
            return model;
        }
        if (photographer.getUsername() == null || photographer.getUsername().trim().isEmpty()) {
            model.addObject("mensagem", "Erro: O Username é Obrigatório");
            return model;
        }

        if (photographer.getEmail() == null || photographer.getEmail().trim().isEmpty()) {
            model.addObject("mensagem", "Erro: O email é obrigatório!");
            model.setViewName("register-form");
            return model;
        }

        if (photographer.getPassword() == null || photographer.getPassword().trim().isEmpty()) {
            model.addObject("mensagem", "Erro: A senha é obrigatória!");
            model.setViewName("register-form");
            return model;
        }

        if (photographer.getCity() == null || photographer.getCity().trim().isEmpty()) {
            model.addObject("mensagem", "Erro: A cidade é obrigatória!");
            model.setViewName("register-form");
            return model;
        }

        if (photographer.getCountry() == null || photographer.getCountry().trim().isEmpty()) {
            model.addObject("mensagem", "Erro: O país é obrigatório!");
            model.setViewName("register-form");
            return model;
        }

        // Verifica se já existe um fotógrafo com o mesmo username
        if (photographerService.existsByUsername(photographer.getUsername())) {
            model.addObject("mensagem", "Erro: Este nome de usuário já está cadastrado!");
            model.setViewName("register-form");
            return model;
        }

        // Verifica se já existe um fotógrafo com o mesmo e-mail
        if (photographerService.findByEmail(photographer.getEmail()) != null) {
            model.addObject("mensagem", "Erro: Este e-mail já está cadastrado!");
            model.setViewName("register-form");
            return model;
        }
        try {
            // Salva o fotógrafo no banco de dados
            Photographer newPhotographer = photographerService.save(photographer);

            // Processa o upload da foto de perfil (se houver)
            if (!file.isEmpty()) {
                newPhotographer.setProfile_photo(file.getBytes());
                PhotoDTO savedPhoto = photoService.addPhoto(newPhotographer, file.getBytes(), true);
                Photo photo = photoService.findById(savedPhoto.getId());
                photo.setImageUrl("http://localhost:8080/photo/" + photo.getId() + "/image");
                photoService.save(photo);
            }

            // Redirecionamento e mensagem de sucesso
            session.setAttribute("user_id", newPhotographer.getId());
            redAttrs.addFlashAttribute("success", "Você foi cadastrado!");
            redAttrs.addFlashAttribute("session", session);
            model.setViewName("redirect:/");

        } catch (IllegalArgumentException e) {
            model.setViewName("register-form");
            model.addObject("mensagem", e.getMessage());
        } catch (Exception e) {
            model.setViewName("register-form");
            model.addObject("mensagem", "Erro ao cadastrar fotógrafo: " + e.getMessage());
        }

        return model;
    }


    //retorna a página de login
    @GetMapping("/login")
    public String getLoginPage(Model model, @ModelAttribute("photographerData") PhotographerDTO photographerData) {
        model.addAttribute("photographerData", photographerData);
        return "login";
    }

    //salva o id do usuário que está fazendo o login na sessão do HTTP
    @PostMapping("/login")
    public String login(@Valid PhotographerDTO photographerData, BindingResult validation,
                        HttpSession session, RedirectAttributes ra)  {
        if (validation.hasErrors()) {
            ra.addFlashAttribute("loginError", "Preencha todos os campos corretamente.");
            return "redirect:/photographer/login";
        }

        Photographer photographer = photographerService.findByEmail(photographerData.getEmail());

        // Verifica se o fotógrafo foi encontrado
        if (photographer == null) {
            ra.addFlashAttribute("loginError", "emailNotFound");
            return "redirect:/photographer/login";
        }

        // Verifica se a senha esta certa
        if (!photographer.getPassword().equals(photographerData.getPassword())) {
            ra.addFlashAttribute("loginError", "wrongPassword");
            return "redirect:/photographer/login";
        }

        session.setAttribute("user_id", photographer.getId());
        session.setAttribute("is_adm", photographer.is_adm());
        return "redirect:/";
    }


    // retorna o perfil do usuário

    @GetMapping("/{photographerId}/profile")
    public String viewProfile(@PathVariable("photographerId") Integer id,
                              HttpSession session,
                              Model model) {

        Integer userLogged = userLogged(session);
        if (userLogged == null) {
            return "redirect:/photographer/login";
        }
        Photographer photographerLogged = photographerService.findById(userLogged);

        Photographer photographerDB = photographerService.findById(id);
        if (photographerDB == null) {
            return "redirect:/error";
        }

        String nickname = "@" + photographerDB.getName().toLowerCase().replace(" ", "");

        // Verificar se o fotógrafo logado está visualizando o próprio perfil
        boolean isOwnProfile = userLogged.equals(photographerDB.getId());

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

    @PostMapping("/{photographerId}/allow-followers")
    public String allowFollowers(@PathVariable("photographerId") Integer photographerId,
                                 HttpSession session) {
        Integer userLogged = userLogged(session);
        if (userLogged == null) {
            return "redirect:/photographer/login";
        }

        Photographer photographerDB = photographerService.findById(photographerId);

        if (photographerDB == null) {
            return "redirect:/photographer/register";
        }

        if (userLogged.equals(photographerId)) {
            photographerService.followAllowed(photographerDB);
        }
        return "redirect:/photographer/" + photographerId + "/profile";
    }

    // seguir um fotógrafo
    @PostMapping("/{photographerId}/follow")
    public String follow(@PathVariable("photographerId") Integer photographerId,
                         HttpSession session) {
        Integer userLogged = userLogged(session);
        if (userLogged == null) {
            return "redirect:/photographer/login";
        }
        try {
            photographerService.follow(photographerId, userLogged);
        } catch (IllegalStateException e) {
            // Trate o caso em que followAllowed é falso
        }
        return "redirect:/photographer/" + photographerId + "/profile";
    }

    @PostMapping("/{photographerId}/unfollow")
    public String unfollow(@PathVariable("photographerId") Integer photographerId,
                           HttpSession session) {
        Integer userLogged = userLogged(session);
        if (userLogged == null) {
            return "redirect:/photographer/login";
        }
        photographerService.unfollow(photographerId, userLogged);
        return "redirect:/photographer/" + photographerId + "/profile";
    }

    //retira o id do usuário da sessão HTTP
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/photographer/login";
    }

    @GetMapping("/suspended")
    public String getSuspendedPage() {
        return "/suspended";
    }

    //retorna o id do usuário logado ou null caso "user_id" esteja vazio
    public Integer userLogged(HttpSession session) {
        return (Integer) session.getAttribute("user_id");
    }
}
