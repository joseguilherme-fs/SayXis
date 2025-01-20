package br.edu.ifpb.pweb2.sayxis.controller;

import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import br.edu.ifpb.pweb2.sayxis.model.PhotographerDTO;
import br.edu.ifpb.pweb2.sayxis.repository.PhotographerRepository;
import br.edu.ifpb.pweb2.sayxis.service.PhotographerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;



@Controller
@RequestMapping("/photographer")
public class PhotographerController {

    @Autowired
    PhotographerService photographerService;

     @Autowired
    private PhotographerRepository photographerRepository;

    @GetMapping("/cadastro")
    public ModelAndView getForm(Photographer photographer, ModelAndView model) {
        model.addObject("photographer", photographer);
        model.setViewName("/cadastro-form");
        return model;
    }

    @PostMapping("/cadastro")
    public ModelAndView cadastrar(@Valid Photographer photographer, BindingResult validation, ModelAndView model,
                                              RedirectAttributes redAttrs) {
        try {
            // Verificação de erros de validação
            if (validation.hasErrors()) {
                model.setViewName("/cadastro-form");
                model.addObject("photographer", photographer);
                return model;
            }

            // Configuração de campos opcionais, caso não estejam preenchidos
            if (photographer.getCity() == null) {
                photographer.setCity("Cidade não informada");
            }
            if (photographer.getCountry() == null) {
                photographer.setCountry("País não informado");
            }

            // Salvando o fotógrafo
            photographerRepository.save(photographer);

            // Redirecionamento e mensagem de sucesso
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



    @GetMapping("/login")
    public String  Page(Model model, @ModelAttribute("photographerData") PhotographerDTO photographerData) {
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
    public Integer isLogged(HttpSession session) {
        return (Integer) session.getAttribute("user_id");
    }
}

