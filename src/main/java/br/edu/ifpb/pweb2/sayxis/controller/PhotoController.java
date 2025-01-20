package br.edu.ifpb.pweb2.sayxis.controller;

import br.edu.ifpb.pweb2.sayxis.model.*;
import br.edu.ifpb.pweb2.sayxis.repository.*;
import br.edu.ifpb.pweb2.sayxis.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "hashtags", required = false) String hashtags,
            HttpSession session,
            Model model
    ) {
            Integer idPhotographer = photographerController.isLogged(session);
            Photographer photographer = photographerService.findById(idPhotographer);

            // Salva a foto
            Photo photo = new Photo();
            photo.setPhotographer(photographer);
            photo.setImageUrl(null);
            photo.setImageData(file.getBytes());
            Photo savedPhoto = photoService.save(photo);

            // Adiciona hashtags, se houver
            if (hashtags != null && !hashtags.isEmpty()) {
                String[] tagsArray = hashtags.split(",");
                for (String tag : tagsArray) {
                    String TagName = tag.trim().toLowerCase();
                    if (!TagName.isEmpty()) {
                        // Cria uma nova tag
                        Tag newTag = new Tag();
                        newTag.setTagName(TagName);
                        Tag savedTag = tagService.save(newTag);

                        // Cria um objeto PhotoTagId
                        PhotoTagId photoTagId = new PhotoTagId();
                        photoTagId.setPhoto_id(savedPhoto.getId());
                        photoTagId.setTag_id(savedTag.getId());

                        // Cria o relacionamento PhotoTag
                        PhotoTag photoTag = new PhotoTag();
                        photoTag.setId(photoTagId);
                        photoTag.setPhoto(savedPhoto);
                        photoTag.setTag(savedTag);

                        photoTagService.save(photoTag);
                    }
                }
            }

            // Adiciona descrição como comentário, se houver
            if (description != null && !description.isEmpty()) {
                Comment comment = new Comment();
                comment.setPhotographer(photographer);
                comment.setPhoto(photo);
                comment.setCommentText(description);
                comment.setCreatedAt(LocalDateTime.now());
                commentService.save(comment);
            }

            model.addAttribute("successMessage", "Envio da foto realizado com sucesso.");

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
