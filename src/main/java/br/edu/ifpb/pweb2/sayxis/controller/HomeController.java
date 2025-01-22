package br.edu.ifpb.pweb2.sayxis.controller;

import br.edu.ifpb.pweb2.sayxis.model.Photo;
import br.edu.ifpb.pweb2.sayxis.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private PhotoService photoService;

    @GetMapping("/")
    public String showHomePage(Model model) {
        List<Photo> photos = photoService.findAll();
        Collections.shuffle(photos);
        model.addAttribute("photos", photos);
        return "index";
    }
}

