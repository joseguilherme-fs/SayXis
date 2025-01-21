package br.edu.ifpb.pweb2.sayxis.controller;

import br.edu.ifpb.pweb2.sayxis.model.*;
import br.edu.ifpb.pweb2.sayxis.model.dto.PhotoDTO;
import br.edu.ifpb.pweb2.sayxis.repository.*;
import br.edu.ifpb.pweb2.sayxis.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Controller
@RequestMapping("/photos")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @Autowired
    private PhotographerController photographerController;

    @Autowired
    private TagService tagService;

    @Autowired
    private PhotoTagService photoTagService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PhotographerService photographerService;

    @GetMapping("/create")
    public String showCreatePhotoForm() {
        return "photo-form";
    }

    @PostMapping("/create")
    public String createPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "caption", required = false) String caption,
            @RequestParam(value = "hashtags", required = false) String hashtags,
            HttpSession session,
            Model model
    ) {
        try {
            Integer idPhotographer = photographerController.isLogged(session);
            Photographer photographer = photographerService.findById(idPhotographer);

            PhotoDTO savedPhoto = photoService.addPhoto(photographer, file.getBytes());

            // Adiciona hashtags, se houver
            if (hashtags != null && !hashtags.isEmpty()) {
                String[] tagsArray = hashtags.split(",");
                for (String tag : tagsArray) {
                    String TagName = tag.trim().toLowerCase();
                    if (!TagName.isEmpty()) {
                        // Cria uma nova tag
                        Tag savedTag = tagService.addTag(TagName);

                        // Cria um objeto PhotoTagId
                        PhotoTagId savedPhotoTagId = tagService.addPhotoTagId(savedPhoto.getId(), savedTag.getId());

                        // Cria o relacionamento PhotoTag
                        tagService.addPhotoTag(savedPhotoTagId,savedPhoto,savedTag);
                    }
                }
            }
            // Adiciona descrição como comentário, se houver
            if (caption!= null && !caption.isEmpty()) {
                commentService.addComment(photographer, savedPhoto, caption);
            }
            model.addAttribute("successMessage", "Envio da foto realizado com sucesso.");

        } catch (IOException e) {
            model.addAttribute("errorMessage", "Erro ao enviar a foto: " + e.getMessage());
        }

        return "redirect:/photos/create";
    }

    @GetMapping("/photos/{photo_id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer photo_id) {
        Photo photo = photoService.findById(photo_id);
        if (photo != null) {
            byte[] imageData = photo.getImageData();
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageData);
        }
        return null;
    }
}
