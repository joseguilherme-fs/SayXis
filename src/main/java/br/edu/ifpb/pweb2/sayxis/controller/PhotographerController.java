package br.edu.ifpb.pweb2.sayxis.controller;

import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import br.edu.ifpb.pweb2.sayxis.model.PhotographerDTO;
import br.edu.ifpb.pweb2.sayxis.service.PhotographerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/photographer")
public class PhotographerController {

    @Autowired
    PhotographerService photographerService;

    @GetMapping("/login")
    public String getLoginPage(Model model, @ModelAttribute("photographerData") PhotographerDTO photographerData) {
        model.addAttribute("photographerData", photographerData);
        return "login";
    }

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

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/photographer/login";
    }

    // retorna null caso o atributo "user_id" estiver vazio
    public String isLogged(HttpSession session) {
        return (String) session.getAttribute("user_id");
    }
}
