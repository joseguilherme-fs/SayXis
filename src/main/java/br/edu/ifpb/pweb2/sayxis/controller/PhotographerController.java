package br.edu.ifpb.pweb2.sayxis.controller;

import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import br.edu.ifpb.pweb2.sayxis.model.dto.PhotographerDTO;
import br.edu.ifpb.pweb2.sayxis.service.PhotoService;
import br.edu.ifpb.pweb2.sayxis.service.PhotographerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
                                             Photographer photographer,
                                             ModelAndView model,
                                             HttpSession session,
                                             RedirectAttributes redAttrs) {
        try {
            //verifica manualmente os campos obrigatórios
            if (
                    photographer.getName() == null || photographer.getName().isEmpty() ||
                    photographer.getEmail() == null || photographer.getEmail().isEmpty() ||
                    photographer.getPassword() == null || photographer.getPassword().isEmpty()) {

                throw new IllegalArgumentException("Erro: nome, email e senha são obrigatórios.");
            }

            //salva o fotógrafo, adiciona foto de perfil (se houver) e trata os campos não obrigatórios
            if (photographer.getCity().isEmpty()) {
                photographer.setCity(null);
            }
            if (photographer.getCountry().isEmpty()) {
                photographer.setCountry(null);
            }
            Photographer newPhotographer = photographerService.save(photographer);

            if(!file.isEmpty()) {
                newPhotographer.setProfile_photo(file.getBytes());
                photoService.addPhoto(newPhotographer, file.getBytes(), true);
            }

            //redirecionamento e mensagem de sucesso
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
    public String login(PhotographerDTO photographerData, HttpSession session, RedirectAttributes ra) {
        Photographer photographer = photographerService.findByEmail(photographerData.getEmail());
        if (photographer == null) {
            ra.addFlashAttribute("loginError", "emailNotFound");
            ra.addFlashAttribute("photographerData", photographerData);
            return "redirect:/photographer/login";
        }

        if(!photographer.getPassword().equals(photographerData.getPassword())) {
            ra.addFlashAttribute("loginError", "wrongPassword");
            ra.addFlashAttribute("photographerData", photographerData);
            return "redirect:/photographer/login";
        }

        session.setAttribute("user_id", photographer.getId());
        session.setAttribute("is_adm", photographer.is_adm());
        return "/index";
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
        model.addAttribute("nickname", nickname);
        model.addAttribute("qntFollowers", photographerService.getFollowersCount(id));
        model.addAttribute("qntFollowing", photographerService.getFollowingCount(id));
        model.addAttribute("qntPosts", photoService.countPhotosByPhotographerId(id));
        model.addAttribute("isOwnProfile", isOwnProfile);
        model.addAttribute("followAllowed", photographerDB.isFollowAllowed());
        model.addAttribute("isFollowing", photographerService.isFollowing(photographerDB, photographerLogged));

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
            return "redirect:/photographer/cadastro";
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
        Photographer photographerLogado = photographerService.findById(userLogged);
        Photographer photographerDB = photographerService.findById(photographerId);

        if (photographerDB == null) {
            return "redirect:/photographer/cadastro";
        }
        photographerService.follow(photographerId, userLogged);
        return "redirect:/photographer/" + photographerId + "/profile";
    }

    //retira o id do usuário da sessão HTTP
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/photographer/login";
    }

    //retorna o id do usuário logado ou null caso "user_id" esteja vazio
    public Integer userLogged(HttpSession session) {
        return (Integer) session.getAttribute("user_id");
    }
}
