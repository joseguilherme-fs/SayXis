package br.edu.ifpb.pweb2.sayxis.controller;

import br.edu.ifpb.pweb2.sayxis.model.*;
import br.edu.ifpb.pweb2.sayxis.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;


@Controller
@RequestMapping("/photos")
public class PhotoController {

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PhotographerController photographerController;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PhotoTagRepository photoTagRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PhotographerRepository photographerRepository;

    @GetMapping("/create")
    public String showCreatePhotoForm() {
        return "photoForm";
    }

    @PostMapping("/create")
    public String createPhoto(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "hashtags", required = false) String hashtags,
            HttpSession session,
            Model model
    ) {
        try {
            Integer idPhotographer = photographerController.isLogged(session);
            Photographer photographer = photographerRepository.findById(idPhotographer)
                    .orElseThrow(() -> new IllegalArgumentException("Photographer not found"));

            // Salva a foto
            Photo photo = new Photo();
            photo.setPhotographer(photographer);
            photo.setImageUrl(null);
            photo.setImageData(file.getBytes());
            Photo savedPhoto = photoRepository.save(photo);

            // Adiciona hashtags, se houver
            if (hashtags != null && !hashtags.isEmpty()) {
                String[] tagsArray = hashtags.split(",");
                for (String tag : tagsArray) {
                    String TagName = tag.trim();
                    if (!TagName.isEmpty()) {
                        // Cria uma nova tag
                        Tag newTag = new Tag();
                        newTag.setTagName(TagName);
                        Tag savedTag = tagRepository.save(newTag);

                        // Cria um objeto PhotoTagId
                        PhotoTagId photoTagId = new PhotoTagId();
                        photoTagId.setPhoto_id(savedPhoto.getId());
                        photoTagId.setTag_id(savedTag.getId());

                        // Cria o relacionamento PhotoTag
                        PhotoTag photoTag = new PhotoTag();
                        photoTag.setId(photoTagId);
                        photoTag.setPhoto(savedPhoto); // savedPhoto é a foto salva
                        photoTag.setTag(savedTag);

                        photoTagRepository.save(photoTag);
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
                commentRepository.save(comment);
            }

            model.addAttribute("successMessage", "Envio da foto realizado com sucesso.");

        } catch (IOException e) {
            model.addAttribute("errorMessage", "Erro ao enviar a foto: " + e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/photos/create"; // Redireciona para o formulário de criação de postagem
    }
}
