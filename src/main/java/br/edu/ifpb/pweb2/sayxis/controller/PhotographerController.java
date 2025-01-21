package br.edu.ifpb.pweb2.sayxis.controller;

import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import br.edu.ifpb.pweb2.sayxis.model.dto.PhotographerDTO;
import br.edu.ifpb.pweb2.sayxis.service.PhotographerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/photographer")
public class PhotographerController {

    @Autowired
    PhotographerService photographerService;

    //retorna a página de cadastro de fotógrafo
    @GetMapping("/cadastro")
    public ModelAndView getForm(Photographer photographer, BindingResult validation, ModelAndView model) {
        model.addObject("photographer", photographer);
        model.setViewName("cadastro-form");
        return model;
    }

    //cadastra um fotógrafo
    @PostMapping("/cadastro")
    public ModelAndView cadastrar( Photographer photographer, ModelAndView model,
                                              RedirectAttributes redAttrs) {
        try {
            //verifica manualmente os campos obrigatórios
            if (
                    photographer.getName() == null || photographer.getName().isEmpty() ||
                    photographer.getEmail() == null || photographer.getEmail().isEmpty() ||
                    photographer.getPassword() == null || photographer.getPassword().isEmpty()) {

                throw new IllegalArgumentException("Erro: Nome, email e senha são obrigatórios.");
            }

            //configura campos opcionais, caso não estejam preenchidos
            if (photographer.getCity() == null) {
                photographer.setCity("Cidade não informada");
            }
            if (photographer.getCountry() == null) {
                photographer.setCountry("País não informado");
            }

            //salva o fotógrafo
            photographerService.save(photographer);

            //redirecionamento e mensagem de sucesso
            redAttrs.addFlashAttribute("mensagem", "Fotógrafo cadastrado com sucesso!");
            model.setViewName("redirect:/cadastro-form");
        } catch (IllegalArgumentException e) {
            model.setViewName("/cadastro-form");
            model.addObject("mensagem", e.getMessage());
        } catch (Exception e) {
            model.setViewName("/cadastro-form");
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
