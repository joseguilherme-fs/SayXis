package br.edu.ifpb.pweb2.sayxis.controller;

import br.edu.ifpb.pweb2.sayxis.model.Photo;
import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import br.edu.ifpb.pweb2.sayxis.service.PhotoService;
import br.edu.ifpb.pweb2.sayxis.service.PhotographerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private PhotoService photoService;

    @GetMapping("/home")
    public String showHomePage(Model model,
                               @RequestParam(defaultValue = "0")int page,
                               @RequestParam(defaultValue = "10")int size) {
        Pageable pageable = PageRequest.of(page,size);

        //busca as fotos paginadas
        Page<Photo> photosPage = photoService.findAll(pageable);

        // Adiciona os atributos ao modelo
        model.addAttribute("photos", photosPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", photosPage.getTotalPages());
        model.addAttribute("totalItems", photosPage.getTotalElements());

        return "index";
    }
}

