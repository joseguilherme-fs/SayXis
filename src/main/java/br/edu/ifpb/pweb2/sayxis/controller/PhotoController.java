package br.edu.ifpb.pweb2.sayxis.controller;

import br.edu.ifpb.pweb2.sayxis.model.*;
import br.edu.ifpb.pweb2.sayxis.model.dto.PhotoDTO;
import br.edu.ifpb.pweb2.sayxis.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Controller
@RequestMapping("/photo")
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @Autowired
    private PhotographerController photographerController;

    @Autowired
    private TagService tagService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PhotographerService photographerService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private PhotoTagService photoTagService;

    //retorna a página de criação da publicação
    @GetMapping("/upload")
    public String showUploadPhotoForm() {
        return "/photo-form";
    }

    //cria uma publicação
    @PostMapping("/upload")
    public String uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "caption", required = false) String caption,
            @RequestParam(value = "hashtags", required = false) String hashtags,
            HttpSession session,
            Model model
    ) {
        try {
            Integer idPhotographer = photographerController.userLogged(session);
            Photographer photographer = photographerService.findById(idPhotographer);
            PhotoDTO savedPhoto = photoService.addPhoto(photographer, file.getBytes());

            //adiciona hashtags, se houver
            if (hashtags != null && !hashtags.isEmpty()) {
                String[] tagsArray = hashtags.split(",");
                String TagName;
                for (String tag : tagsArray) {
                    TagName = tag.trim().toLowerCase();
                    if (!TagName.isEmpty()) {
                        //cria uma nova tag
                        Tag savedTag = tagService.addTag(TagName);

                        //cria um objeto PhotoTagId
                        PhotoTagId savedPhotoTagId = tagService.addPhotoTagId(savedPhoto.getId(), savedTag.getId());

                        //cria o relacionamento PhotoTag
                        tagService.addPhotoTag(savedPhotoTagId,savedPhoto,savedTag);
                    }
                }
            }
            //adiciona legenda como comentário, se houver
            if (caption!= null && !caption.isEmpty()) {
                commentService.addComment(photographer, savedPhoto, caption);
            }
            model.addAttribute("successMessage", "Envio da foto realizado com sucesso.");

        } catch (IOException e) {
            model.addAttribute("errorMessage", "Erro ao enviar a foto: " + e.getMessage());
        }

        return "redirect:/photo/upload";
    }

    //retorna a página da foto
    @GetMapping("/{photo_id}")
    public String getPhoto(Model model, HttpSession session, @PathVariable Integer photo_id) {
        String link_photo = "http://localhost:8080/photos/" + photo_id + "/image";
        Integer photographer_id = (Integer) session.getAttribute("user_id");
        model.addAttribute("linkPhoto", link_photo);
        model.addAttribute("numberOfLikes", likeService.countLikes(photo_id));
        model.addAttribute("isLiked", likeService.isLiked(photographer_id, photo_id));
        model.addAttribute("comments", commentService.getComments(photo_id));
        model.addAttribute("tags", photoTagService.getTags(photo_id));
        if (commentService.getCaption(photo_id) != null) {
            model.addAttribute("caption", commentService.getCaption(photo_id).getCommentText());
        }
        return "/photo";
    }

    //retorna a imagem da página da foto
    @GetMapping("/{photo_id}/image")
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
