package br.edu.ifpb.pweb2.sayxis.controller;

import br.edu.ifpb.pweb2.sayxis.model.Photographer;
import br.edu.ifpb.pweb2.sayxis.service.PhotographerService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.core.userdetails.UserDetails;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final PhotographerService photographerService;

    public GlobalControllerAdvice(PhotographerService photographerService) {
        this.photographerService = photographerService;
    }

    // retorna o id do fotógrafo globlamente, pode ser utilizado em qualquer view pelo atributo abaixo:
    @ModelAttribute("authPhotographerId")
    public Integer getAuthenticatedPhotographerId(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            Photographer photographer = photographerService.findByUsername(userDetails.getUsername());
            return photographer != null ? photographer.getId() : null;
        }
        return null;
    }

    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception e) {
        return "error";
    }
}
