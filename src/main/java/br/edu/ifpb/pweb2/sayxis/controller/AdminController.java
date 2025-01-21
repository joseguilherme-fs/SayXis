package br.edu.ifpb.pweb2.sayxis.controller;

import br.edu.ifpb.pweb2.sayxis.repository.PhotographerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping
public class AdminController {

    @Autowired
    PhotographerRepository photographerService;

    @GetMapping("/admin")
    public String adminPage(){
        return "/admin";
    }

    @GetMapping("/admin/acess-denied")
    public String acessDenied(){
        return "access-denied";
    }

    @GetMapping("admin/list-users")
    public String listUsers(Model model, RedirectAttributes ra, HttpSession session){
        if(session.getAttribute("user_id") == null){
            ra.addFlashAttribute("httpStatusCode", "401");
            return "redirect:/admin/acess-denied";
        } else if (session.getAttribute("is_adm").equals(Boolean.FALSE)){
            ra.addFlashAttribute("httpStatusCode", "403");
            return "redirect:/admin/acess-denied";
        }

        model.addAttribute("photographers", photographerService.findAll());
        return "/admin";
    }
}
